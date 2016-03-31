import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;


public class EmployeesModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = {"ID", "Name", "Title", "Manager"};
	
	private List<Employee> data;
	
	private HashMap<Integer, Employee> dataMap;
	
	
	EmployeesModel(List<Employee> data) {
		this.data = data;
	}
	
	EmployeesModel(HashMap<Integer, Employee> dataMap) {
		this.dataMap = dataMap;
	}


	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch(col) {
		case 0:
			return data.get(row).getId();
		case 1: 
			return data.get(row).getName();
		case 2:
			return data.get(row).getTitle();
		case 3:
			return data.get(row).getManager();
		default:
			return null;
		}
	}

}
