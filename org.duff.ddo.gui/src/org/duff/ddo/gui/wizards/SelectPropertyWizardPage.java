package org.duff.ddo.gui.wizards;

import java.util.List;

import org.duff.ddo.crafting.data.PropertyDAO;
import org.duff.ddo.crafting.model.Property;
import org.duff.ddo.gui.table.PriorityEditingSupport;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class SelectPropertyWizardPage extends WizardPage {

	private final List<Property> properties;
	
	private CheckboxTableViewer checkboxTableViewer;
	private Table table;
	private Text textFilter;
	private ViewerFilter filter;
	private String filterString;
	
	public SelectPropertyWizardPage(List<Property> properties) {
		super("wizardPage");
		setTitle("Select Properties");
		setDescription("Please select the desidered properties.");
		
		this.properties = properties;
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		textFilter = new Text(container, SWT.BORDER);
		textFilter.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				filterString = textFilter.getText().toUpperCase();
				checkboxTableViewer.refresh();
			}
		});
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		checkboxTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn columnName = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		columnName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Property property = (Property) element;
				return property.getName();
			}
		});
		TableColumn tblclmnName = columnName.getColumn();
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		
		TableViewerColumn columnPriority = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		columnPriority.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Property property = (Property) element;
				return Integer.toString(property.getPriority());
			}
		});
		columnPriority.setEditingSupport(new PriorityEditingSupport(checkboxTableViewer));
		TableColumn tblclmnPriority = columnPriority.getColumn();
		tblclmnPriority.setWidth(100);
		tblclmnPriority.setText("Priority");
		
		filterString = "";
		filter = new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				Property p = (Property) element;
				boolean select = p.getName().toUpperCase().contains(filterString);
				return select;
			}
			
		};
		checkboxTableViewer.setFilters(filter);
		
		checkboxTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		checkboxTableViewer.setInput(PropertyDAO.getInstance().getProperties());
		for (TableColumn tc : table.getColumns()) {
			tc.pack();
		}
	}
	
	@Override
	public IWizardPage getNextPage() {
		properties.clear();
		for (Object selectedItem : checkboxTableViewer.getCheckedElements()) {
			Property property = (Property) selectedItem;
			properties.add(property);
		}
		return super.getNextPage();
	}

}
