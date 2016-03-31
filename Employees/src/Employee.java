import java.util.List;


public class Employee {
	
	private String name;
	private String title;
	
	private Employee manager;
	private List<Employee> subordinates;
	
	public Employee() {
		
	}
	
	public Employee(String name, String title, Employee manager) {
		this.name = name;
		this.title = title;
		this.manager = manager;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String toString() {
		return getTitle();
	}

	public Employee getManager() {
		return manager;
	}
	public void setManager(Employee manager) {
		this.manager = manager;
	}
	public List<Employee> getSubordinates() {
		return subordinates;
	}
	public void setSubordinates(List<Employee> subordinates) {
		this.subordinates = subordinates;
	}

}
