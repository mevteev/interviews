import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;


public class EmployeesModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = {"ID", "Name", "Title", "Manager"};
	
	
	//private List<Employee> data;
	
	private HashMap<Integer, Employee> dataMap;
	private Catalog catalog;
	
	
	/*
	EmployeesModel(List<Employee> data) {
		this.data = data;
	}*/
	
	EmployeesModel(Catalog catalog) {
		this.dataMap = catalog.getEmployeesMap();
		this.catalog = catalog;
	}


	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public int getRowCount() {
		//return data.size();
		//return dataMap.size();
		return catalog.getIdx().size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		
		
		Integer index = (Integer) catalog.getIdx().toArray()[row];
		
		Employee bufferEmp = catalog.getBufferEntryById(index);
		Employee emp;
		if (bufferEmp == null) {
			emp = dataMap.get(index);
		} else {
			emp = bufferEmp;
		}
		
		switch(col) {
		case 0:
			return emp.getId();
		case 1: 
			return emp.getName();
		case 2:
			return emp.getTitle();
		case 3:
			return emp.getManager();
		default:
			return null;
		}
	}

}
