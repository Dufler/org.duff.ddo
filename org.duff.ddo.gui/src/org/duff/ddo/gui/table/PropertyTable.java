package org.duff.ddo.gui.table;

import org.duff.ddo.crafting.ESlot;
import org.duff.ddo.crafting.data.PropertyDAO;
import org.duff.ddo.crafting.model.Property;
import org.duff.ddo.gui.dialogs.PropertyDialog;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class PropertyTable extends TableViewer {
	
	private Table table;
	private ViewerFilter filter;
	private String filterString;

	public PropertyTable(Composite parent) {
		super(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		
		table = getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				modifyProperty();
			}
		});
		
		filterString = "";
		filter = new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				Property p = (Property) element;
				boolean select = p.getName().toUpperCase().contains(filterString);
				return select;
			}
			
		};
		setFilters(filter);
		
		Menu menu = new Menu(table);
		
		MenuItem newPropertyItem = new MenuItem(menu, SWT.POP_UP);
		newPropertyItem.setText("New Property");
		newPropertyItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewProperty();
			}
		});
		
		MenuItem modifyPropertyItem = new MenuItem(menu, SWT.POP_UP);
		modifyPropertyItem.setText("Modify Property");
		modifyPropertyItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				modifyProperty();
			}
		});
		
		MenuItem deletePropertyItem = new MenuItem(menu, SWT.POP_UP);
		deletePropertyItem.setText("Delete Property");
		deletePropertyItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteProperty();
			}
		});
		
		table.setMenu(menu);
		
		
		TableViewerColumn columnName = new TableViewerColumn(this, SWT.NONE);
		columnName.setLabelProvider(new ColumnLabelProvider() {
			@Override
		    public String getText(Object element) {
				Property p = (Property) element;
		        return p.getName();
		    }
		});
		TableColumn tblclmnNome = columnName.getColumn();
		tblclmnNome.setWidth(100);
		tblclmnNome.setText("Name");
		
		TableViewerColumn columnPrefix = new TableViewerColumn(this, SWT.NONE);
		columnPrefix.setLabelProvider(new ColumnLabelProvider() {
			@Override
		    public String getText(Object element) {
				Property p = (Property) element;
				String testo = "";
				for (ESlot slot : p.getPrefix()) {
					testo += slot + ", ";
				}
				if (!testo.isEmpty())
					testo = testo.substring(0, testo.length() - 2);
		        return testo;
		    }
		});
		TableColumn tblclmnPrefix = columnPrefix.getColumn();
		tblclmnPrefix.setWidth(100);
		tblclmnPrefix.setText("Prefix");
		
		TableViewerColumn columnSuffix = new TableViewerColumn(this, SWT.NONE);
		columnSuffix.setLabelProvider(new ColumnLabelProvider() {
			@Override
		    public String getText(Object element) {
				Property p = (Property) element;
				String testo = "";
				for (ESlot slot : p.getSuffix()) {
					testo += slot + ", ";
				}
				if (!testo.isEmpty())
					testo = testo.substring(0, testo.length() - 2);
		        return testo;
		    }
		});
		TableColumn tblclmnSuffix = columnSuffix.getColumn();
		tblclmnSuffix.setWidth(100);
		tblclmnSuffix.setText("Suffix");
		
		TableViewerColumn columnExtra = new TableViewerColumn(this, SWT.NONE);
		columnExtra.setLabelProvider(new ColumnLabelProvider() {
			@Override
		    public String getText(Object element) {
				Property p = (Property) element;
				String testo = "";
				for (ESlot slot : p.getExtra()) {
					testo += slot + ", ";
				}
				if (!testo.isEmpty())
					testo = testo.substring(0, testo.length() - 2);
		        return testo;
		    }
		});
		TableColumn tblclmnExtra = columnExtra.getColumn();
		tblclmnExtra.setWidth(100);
		tblclmnExtra.setText("Extra");

		setContentProvider(ArrayContentProvider.getInstance());
		setContent();
	}
	
	public void setContent() {
		setInput(PropertyDAO.getInstance().getProperties());
		for (TableColumn tc : table.getColumns()) {
			tc.pack();
		}
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		if (filterString == null)
			filterString = "";
		this.filterString = filterString.toUpperCase();
		refresh();
	}
	
	private void addNewProperty() {
		PropertyDialog dialog = new PropertyDialog();
		int result = dialog.open();
		if (result == Dialog.OK) {
			setContent();
		}
	}
	
	private void modifyProperty() {
		int selectionIndex = table.getSelectionIndex();
		if (selectionIndex != -1) {
			Property property = (Property) table.getItem(selectionIndex).getData();
			PropertyDialog dialog = new PropertyDialog(property);
			if (dialog.open() == Dialog.OK) {
				setContent();
			}
		}
	}
	
	private void deleteProperty() {
		int selectionIndex = table.getSelectionIndex();
		if (selectionIndex != -1) {
			Property property = (Property) table.getItem(selectionIndex).getData();
			String title = "Delete Property";
			String message = "Are you really sure to delete property: '" + property.getName() + "' ?";
			boolean confirm = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), title, message);
			if (confirm) {
				boolean delete = PropertyDAO.getInstance().deleteProperty(property);
				if (!delete) {
					String errorTitle = "Deletion failed";
					String errorMessage = "Deletion failed: selected property can't be deleted.";
					MessageDialog.openError(Display.getDefault().getActiveShell(), errorTitle, errorMessage);
				} else {
					setContent();
				}
			}
		}
	}

}
