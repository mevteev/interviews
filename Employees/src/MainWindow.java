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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Savepoint;


public class MainWindow extends JDialog implements TableModelListener {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	
	private EmployeesModel model;
	
	private static MainWindow dialog;
	
	static private Catalog catalog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		catalog = new Catalog();
		
		//catalog.readDB();
		
		try {
			dialog = new MainWindow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.setTitle("Employee dictionary - " + Catalog.getXmlFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MainWindow() {
		
		
		
		setTitle("Employees database");
		setBounds(100, 100, 665, 404);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{70, 68, 90, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{274, 23, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPanel.add(scrollPane, gbc_scrollPane);
		
		//model = new EmployeesModel(catalog.getEmployees());
		model = new EmployeesModel(catalog);
		
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
		gbc_btnAdd.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnAdd.insets = new Insets(0, 0, 0, 5);
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 3;
		contentPanel.add(btnAdd, gbc_btnAdd);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editEmployee();
			}
		});
		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnEdit.insets = new Insets(0, 0, 0, 5);
		gbc_btnEdit.gridx = 1;
		gbc_btnEdit.gridy = 3;
		contentPanel.add(btnEdit, gbc_btnEdit);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteEmployee();
			}
		});
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 0, 0, 5);
		gbc_btnDelete.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnDelete.gridx = 2;
		gbc_btnDelete.gridy = 3;
		contentPanel.add(btnDelete, gbc_btnDelete);
		
		JButton btnSetDataFolder = new JButton("Set data folder");
		btnSetDataFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setDataFolder();
			}
		});
		GridBagConstraints gbc_btnSetDataFolder = new GridBagConstraints();
		gbc_btnSetDataFolder.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnSetDataFolder.insets = new Insets(0, 0, 0, 5);
		gbc_btnSetDataFolder.gridx = 3;
		gbc_btnSetDataFolder.gridy = 3;
		contentPanel.add(btnSetDataFolder, gbc_btnSetDataFolder);
		
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
			
			Employee bufferEmp = catalog.getBufferEntryById(id);
			
			if (bufferEmp != null) {
				curEmp = bufferEmp;
			}
			
			EmployeeDialog eDlg = new EmployeeDialog(curEmp, catalog);
			eDlg.setModal(true);
			Employee newEmp = eDlg.showDialog();		
			if (newEmp != null) {
				/*
				curEmp.setName(newEmp.getName());
				curEmp.setTitle(newEmp.getTitle());
				curEmp.setManager(newEmp.getManager());
				*/
				
				catalog.editEmployee(curEmp, newEmp);
				
				model.fireTableDataChanged();
			}
		}
	}
	
	private void deleteEmployee() {
		if (table.getSelectedRow() != -1) {
			Integer id = (Integer) table.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 0);
			Employee curEmp = catalog.getEmployeeById(id);
			Employee bufferEmp = catalog.getBufferEntryById(id);
			
			if (bufferEmp != null) {
				curEmp = bufferEmp;
			}
			
			if (JOptionPane.showConfirmDialog(null, "Do you want to delete " + curEmp + " ?", "Delete?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				catalog.deleteEmployee(curEmp);
				catalog.getActualIndexes();
				model.fireTableDataChanged();
				
			}
			
		}
	}
	
	private void setDataFolder() {
		JFileChooser fileChooser = new JFileChooser(Catalog.getXmlFile());
		fileChooser.setDialogTitle("Select a file");
		int result = fileChooser.showSaveDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			Catalog.setXmlFile(file.getAbsolutePath());
			dialog.setTitle("Employee dictionary - " + Catalog.getXmlFile());
			catalog.performSave();
			
		}
		
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		//catalog.saveDB();
		catalog.performSave();
		
	}
	
	
}
