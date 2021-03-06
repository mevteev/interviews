import java.util.List;


public class Employee {
	
	private int id;
	private String name;
	private String title;
	
	private Employee manager;
	private List<Employee> subordinates;
	
	private int managerId;


	
	public Employee() {
		
	}
	
	public Employee(String name, String title, Employee manager) {
		this.name = name;
		this.title = title;
		this.manager = manager;
		this.id = Catalog.getNextId();
	}
	
	public Employee(Employee emp) {
		this.name = emp.name;
		this.title = emp.title;
		this.manager = emp.manager;
		this.id = emp.getId();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
		
		if (Catalog.getNextId() <= id) {
			Catalog.setNextId(id + 1);
		}
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
	
	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}
	
	
	public String toString() {
		return getName();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Employee)) {
			return false;
		}
		Employee other = (Employee) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	
	

}
