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
	
	
	/*
	EmployeesModel(List<Employee> data) {
		this.data = data;
	}*/
	
	EmployeesModel(HashMap<Integer, Employee> dataMap) {
		this.dataMap = dataMap;
	}


	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		//return data.size();
		return dataMap.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object[] entries = dataMap.entrySet().toArray();
		@SuppressWarnings("unchecked")
		Map.Entry<Integer, Employee> entry = (Map.Entry<Integer, Employee>)entries[row];  
		
		
		switch(col) {
		case 0:
			return entry.getValue().getId();
		case 1: 
			return entry.getValue().getName();
		case 2:
			return entry.getValue().getTitle();
		case 3:
			return entry.getValue().getManager();
		default:
			return null;
		}
	}

}
