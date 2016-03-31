import java.util.ArrayList;


public class Catalog {
	
	private ArrayList<Employee> employees;
	
	Catalog() {
		employees = new ArrayList<Employee>();
		readDB();
	}
	
	
	private void readDB() {};
	
	private void saveDB() {}


	public ArrayList<Employee> getEmployees() {
		return employees;
	}


	public void setEmployees(ArrayList<Employee> employees) {
		this.employees = employees;
	} ;
	
	public void addEmployee(Employee emp) {
		employees.add(emp);
	}
	
	public Employee getEmployee(int index) {
		return employees.get(index);
	}
	
	

}
