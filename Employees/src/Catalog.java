import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Catalog {
	
	private ArrayList<Employee> employees;
	
	private HashMap<Integer, Employee> employeesMap;
	
	static private int nextId = 0;
	
	static private String xmlFile = "emp.xml";
	
	Catalog() {
		employees = new ArrayList<Employee>();
		
		employeesMap = new HashMap<>();
		
		readDB();
		
	}
	
	public Object[] getEmployeesArr() {
		return employeesMap.values().toArray();
	}
	
	
	public void readDB() {
		BufferedReader br = null; 
		
		try {
			br = new BufferedReader(new FileReader(xmlFile));
			
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
	
	public void saveDB() {
		PrintWriter fs = null;
		try {
			fs = new PrintWriter(xmlFile);
			fs.write("<?xml version=\"1.0\"?>" + System.lineSeparator());
			fs.write("<catalog>"  + System.lineSeparator());
			
			for (Map.Entry<Integer, Employee> entry : employeesMap.entrySet()) {
				Employee emp = entry.getValue();
				
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
		}
		finally {
			if (fs != null) {
				//System.out.println("Saved");
				fs.close();
			}
		}
		
		
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
		employeesMap.put(emp.getId(), emp);
		nextId++;
	}
	
	public void deleteEmployee(Employee emp) {
		employeesMap.remove(emp.getId());
		
		for (Map.Entry<Integer, Employee> entry : employeesMap.entrySet()) {
			Employee employee = entry.getValue();
			if (employee.getManager() == emp) {
				employee.setManager(null);
			}
		}
		
		
	}
	

	public Employee getEmployeeById(int id) {
		return employeesMap.get(id);
	}
	
	

}
