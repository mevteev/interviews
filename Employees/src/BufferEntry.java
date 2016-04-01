import java.util.Date;


public class BufferEntry {
	public enum Status {ADD, EDIT, DELETE };
	
	private Employee emp;
	private Date date;
	private Status status;
	
	
	BufferEntry(Employee emp, Status status) {
		this.setEmp(new Employee(emp));
		this.setStatus(status);
		this.setDate(new Date());
	}


	public Employee getEmp() {
		return emp;
	}


	public void setEmp(Employee emp) {
		this.emp = emp;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}

}
