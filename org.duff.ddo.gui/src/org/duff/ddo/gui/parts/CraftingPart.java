package org.duff.ddo.gui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.duff.ddo.gui.table.PropertyTable;
import org.duff.ddo.gui.wizards.NewItemSetWizard;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class CraftingPart {

	private Text txtFilter;
	private PropertyTable tableViewer;

	@Inject
	private MDirtyable dirty;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		txtFilter = new Text(parent, SWT.BORDER);
		txtFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtFilter.setMessage("Type text to filter properties...");
		txtFilter.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				tableViewer.setFilterString(txtFilter.getText());
			}
		});

		tableViewer = new PropertyTable(parent);
		
		Button btnNewItemSet = new Button(parent, SWT.NONE);
		btnNewItemSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startOPtimizationWizard();
			}
		});
		btnNewItemSet.setText("New Item Set");
		
//		Button btnImportCsv = new Button(parent, SWT.NONE);
//		btnImportCsv.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				CSVImporter csvi = new CSVImporter();
//				csvi.importCSV();
//				tableViewer.refresh();
//			}
//		});
//		btnImportCsv.setText("Import csv");
	}

	@Focus
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
	
	private void startOPtimizationWizard() {
		WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), new NewItemSetWizard());
		dialog.open();
	}
	
}