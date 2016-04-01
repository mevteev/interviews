import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.awt.GridBagLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MainWindow extends JDialog implements TableModelListener {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	
	private EmployeesModel model;
	
	static private Catalog catalog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		catalog = new Catalog();
		
		//catalog.readDB();
		
		try {
			MainWindow dialog = new MainWindow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MainWindow() {
		
		
		
		setTitle("Employees database");
		setBounds(100, 100, 609, 416);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{51, 51, 449, 0};
		gbl_contentPanel.rowHeights = new int[]{274, 23, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPanel.add(scrollPane, gbc_scrollPane);
		
		//model = new EmployeesModel(catalog.getEmployees());
		model = new EmployeesModel(catalog.getEmployeesMap());
		
		table = new JTable(model);
		scrollPane.setViewportView(table);
		table.getModel().addTableModelListener(this);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addEmployee();
			}
		});
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnAdd.insets = new Insets(0, 0, 0, 5);
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 1;
		contentPanel.add(btnAdd, gbc_btnAdd);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editEmployee();
			}
		});
		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnEdit.insets = new Insets(0, 0, 0, 5);
		gbc_btnEdit.gridx = 1;
		gbc_btnEdit.gridy = 1;
		contentPanel.add(btnEdit, gbc_btnEdit);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteEmployee();
			}
		});
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnDelete.gridx = 2;
		gbc_btnDelete.gridy = 1;
		contentPanel.add(btnDelete, gbc_btnDelete);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}
	
	private void addEmployee() {
		EmployeeDialog eDlg = new EmployeeDialog(null, catalog);
		eDlg.setModal(true);
		Employee newEmp = eDlg.showDialog();		
		if (newEmp != null) {
			catalog.addEmployee(newEmp);
			
			model.fireTableDataChanged();
		}
	}
	
	private void editEmployee() {
		if (table.getSelectedRow() != -1) {
			//Employee curEmp = catalog.getEmployee(table.convertRowIndexToModel(table.getSelectedRow()));
			
			Integer id = (Integer) table.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 0);
			Employee curEmp = catalog.getEmployeeById(id);
			
			EmployeeDialog eDlg = new EmployeeDialog(curEmp, catalog);
			eDlg.setModal(true);
			Employee newEmp = eDlg.showDialog();		
			if (newEmp != null) {
				curEmp.setName(newEmp.getName());
				curEmp.setTitle(newEmp.getTitle());
				curEmp.setManager(newEmp.getManager());
				
				model.fireTableDataChanged();
			}
		}
	}
	
	private void deleteEmployee() {
		if (table.getSelectedRow() != -1) {
			Integer id = (Integer) table.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 0);
			Employee curEmp = catalog.getEmployeeById(id);
			
			if (JOptionPane.showConfirmDialog(null, "Do you want to delete " + curEmp + " ?", "Delete?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				catalog.deleteEmployee(curEmp);
				model.fireTableDataChanged();
				
			}
			
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		catalog.saveDB();
		
	}
}
