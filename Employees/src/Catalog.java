import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class Catalog {
	
	private HashMap<Integer, Employee> employeesMap;
	private ArrayList<BufferEntry> buffer;
	
	private Set<Integer> idx;
	
	private boolean isInSavingProcess = false;
	private boolean needToSave = false;
	
	private Date lastSavingTime = new Date();
	
	
	
	static private int nextId = 0;
	
	static private String xmlFile = "emp.xml";
	
	Catalog() {
	
		employeesMap = new HashMap<>();
		
		buffer = new ArrayList<>();
		
		readDB();
		
		setIdx(getActualIndexes());
		
	}
	
	public Object[] getEmployeesArr() {
		LinkedList<Employee> arr = new LinkedList<>();
		for (int i = 0; i < getIdx().size(); i++) {
			Integer index = (Integer) getIdx().toArray()[i];
			
			Employee bufferEmp = getBufferEntryById(index);
			Employee emp;
			if (bufferEmp == null) {
				emp = employeesMap.get(index);
			} else {
				emp = bufferEmp;
			}			
			arr.add(emp);
		}
		return arr.toArray();
	}
	
	
	public void readDB() {
		BufferedReader br = null; 
		
		try {
			br = new BufferedReader(new FileReader(getXmlFile()));
			
			String line = br.readLine();
			
			Employee emp = null;
			
			//parse all data except manager id
			//manager might not be created in memory if manager id > employee id
			while (line != null) {
				if (line.contains("<employee>")) {
					emp = new Employee();
				}
				else if (line.contains("<id>")) {
					int firstpos = line.indexOf('>') + 1;
					int lastpos = line.lastIndexOf('<');
					int id = Integer.parseInt(line.substring(firstpos, lastpos));
					emp.setId(id);
				}
				else if (line.contains("<name>")) {
					int firstpos = line.indexOf('>') + 1;
					int lastpos = line.lastIndexOf('<');
					emp.setName(line.substring(firstpos, lastpos));
				}
				else if (line.contains("<title>")) {
					int firstpos = line.indexOf('>') + 1;
					int lastpos = line.lastIndexOf('<');
					emp.setTitle(line.substring(firstpos, lastpos));
				}
				else if (line.contains("<manager>")) {
					int firstpos = line.indexOf('>') + 1;
					int lastpos = line.lastIndexOf('<');
					int id = Integer.parseInt(line.substring(firstpos, lastpos));
					emp.setManagerId(id);
				}
				else if (line.contains("</employee>")) {
					employeesMap.put(emp.getId(), emp);
				}
				
				line = br.readLine();
			}
			
			// fill managers
			for (Map.Entry<Integer, Employee> entry : employeesMap.entrySet()) {
				entry.getValue().setManager(employeesMap.get(entry.getValue().getManagerId()));
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	};
	
	
	public void performSave() {
		System.out.println("Perform save data");
		needToSave = true;
		if (!isInSavingProcess) {
			isInSavingProcess = true;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					//сохраняем данные. 
					//параметр в Save - время, изменения до которого попадут в файл из буферной таблицы
					while (needToSave) {
						Date date = new Date();
						needToSave = false;
						boolean result = saveDB(date);
						
						if (result == false)  //если не удалось записать, ставим флаг
							needToSave = true;
						else 
							lastSavingTime = date;
					}
					isInSavingProcess = false;
				}
			}).start();
		} else {
			needToSave = true;
		}
	}
	
	public boolean saveDB(Date date) {
		
		System.out.println("Saving data till " + date); 
		
		Set<Integer> idx = getActualIndexes(date);
		
		boolean result = true;
		PrintWriter fs = null;
		try {
			fs = new PrintWriter(getXmlFile());
			fs.write("<?xml version=\"1.0\"?>" + System.lineSeparator());
			fs.write("<catalog>"  + System.lineSeparator());
			

			//for (Map.Entry<Integer, Employee> entry : employeesMap.entrySet()) {
			for (int i = 0; i < getIdx().size(); i++) {
				Integer index = (Integer) getIdx().toArray()[i];
				
				Employee bufferEmp = getBufferEntryById(index);
				Employee emp;
				if (bufferEmp == null) {
					emp = employeesMap.get(index);
				} else {
					emp = bufferEmp;
				}			
				
				fs.write(" <employee>"  + System.lineSeparator());
				fs.write("  <id>" + emp.getId() + "</id>"  + System.lineSeparator());
				fs.write("  <name>" + emp.getName() + "</name>"  + System.lineSeparator());
				fs.write("  <title>" + emp.getTitle() + "</title>"  + System.lineSeparator());
				
				fs.write("  <manager>");
				if (emp.getManager() != null) {
					fs.write(String.valueOf(emp.getManager().getId()));
				} else {
					fs.write("-1");
				};
				fs.write("</manager>"  + System.lineSeparator());
				fs.write(" </employee>"  + System.lineSeparator());
			}
			
			fs.write("</catalog>");
			

		}
		catch (IOException e) {
			e.printStackTrace();
			result = false; //неудача
		}
		finally {
			if (fs != null) {
				//System.out.println("Saved");
				fs.close();
			}
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static int getNextId() {
		return nextId;
	}
	
	public static void setNextId(Integer next) {
		nextId = next;
	}


	public HashMap<Integer, Employee> getEmployeesMap() {
		return employeesMap;
	}
	
	public void addEmployee(Employee emp) {
		//employeesMap.put(emp.getId(), emp);
		buffer.add(new BufferEntry(emp, BufferEntry.Status.ADD));
		nextId++;
		setIdx(getActualIndexes());
	}
	
	public void deleteEmployee(Employee emp) {
		//employeesMap.remove(emp.getId());
		
		buffer.add(new BufferEntry(emp, BufferEntry.Status.DELETE));
		
		//for (Map.Entry<Integer, Employee> entry : employeesMap.entrySet()) {
		for (int i = 0; i < getIdx().size(); i++) {
			Integer index = (Integer) getIdx().toArray()[i];
			
			Employee bufferEmp = getBufferEntryById(index);
			Employee employee;
			if (bufferEmp == null) {
				employee = employeesMap.get(index);
			} else {
				employee = bufferEmp;
			}			
		
			if (employee.getManager() == emp) {
				employee.setManager(null);
				buffer.add(new BufferEntry(employee, BufferEntry.Status.EDIT));
				
			}
		}
		
		setIdx(getActualIndexes());

	}
	
	public void editEmployee(Employee emp, Employee newValues) {
		emp.setManager(newValues.getManager());
		emp.setName(newValues.getName());
		emp.setTitle(newValues.getTitle());
		
		buffer.add(new BufferEntry(emp, BufferEntry.Status.EDIT));
	}
	

	public Employee getEmployeeById(int id) {
		Employee emp = employeesMap.get(id);
		Employee bufferEmp = getBufferEntryById(emp);
		
		if (bufferEmp == null) {		
			return emp;
		} else {
			//System.out.println("From bufffer");
			return bufferEmp;
		}
	}
	
	
	public Employee getBufferEntryById(Employee emp) {
		int index = bufferLastIndex(emp);
		if (index == -1) {
			return null;
		} else {
			return buffer.get(index).getEmp();
		}
		
	}
	
	
	public Employee getBufferEntryById(Employee emp, Date date) {
		int index = bufferLastIndex(emp, date);
		if (index == -1) {
			return null;
		} else {
			return buffer.get(index).getEmp();
		}
		
	}
	
	
	public Employee getBufferEntryById(Integer id) {
		int index = bufferLastIndex(id);
		if (index == -1) {
			return null;
		} else {
			return buffer.get(index).getEmp();
		}
		
	}
	
	public int bufferLastIndex(Employee emp) {
		for (int i = buffer.size() - 1; i>=0; i--) {
			if (buffer.get(i).getEmp().equals(emp))
				return i;
		}
		return -1;
	}
	
	public int bufferLastIndex(Employee emp, Date date) {
		for (int i = buffer.size() - 1; i>=0; i--) {
			if (buffer.get(i).getDate().compareTo(date) <=0 &&  buffer.get(i).getEmp().equals(emp))
				return i;
		}
		return -1;
	}
	
	public int bufferLastIndex(Integer id) {
		for (int i = buffer.size() - 1; i>=0; i--) {
			if (buffer.get(i).getEmp().getId() == id)
				return i;
		}
		return -1;
	}
	
	public Set<Integer> getActualIndexes() {
		TreeSet<Integer> idx = new TreeSet<>(employeesMap.keySet());
		
		
		for (BufferEntry entry : buffer) {
			if (entry.getStatus() == BufferEntry.Status.ADD) {
				idx.add(entry.getEmp().getId());
			}
			else if (entry.getStatus() == BufferEntry.Status.DELETE) {
				idx.remove(entry.getEmp().getId());
			}
		}
		
		return idx;
	}

	public Set<Integer> getIdx() {
		return idx;
	}

	public void setIdx(Set<Integer> idx) {
		this.idx = idx;
	}
	
	public Set<Integer> getActualIndexes(Date date) {
		TreeSet<Integer> idx = new TreeSet<>(employeesMap.keySet());
		
		
		for (BufferEntry entry : buffer) {
			if (entry.getDate().compareTo(date) <= 0 ) { 
				if (entry.getStatus() == BufferEntry.Status.ADD) {
					idx.add(entry.getEmp().getId());
				}
				else if (entry.getStatus() == BufferEntry.Status.DELETE) {
					idx.remove(entry.getEmp().getId());
				}
			}
		}
		
		return idx;
	}

	public static String getXmlFile() {
		return xmlFile;
	}

	public static void setXmlFile(String xmlFile) {
		Catalog.xmlFile = xmlFile;
	}

	
	

}
