package org.duff.ddo.gui.wizards;

import java.util.ArrayList;
import java.util.List;

import org.duff.ddo.crafting.model.Item;
import org.duff.ddo.gui.table.SlotTable;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class SelectSlotWizardPage extends WizardPage {

	private final List<Item> items;
	private CheckboxTableViewer viewer;
	
	public SelectSlotWizardPage(List<Item> items) {
		super("wizardPage");
		setTitle("Select Items Slots");
		setDescription("Please select the avaible item slots, unchecked items won't be used.\r\nYou can already set some desidered properties.");
		
		this.items = items;
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		viewer = SlotTable.getSlotTable(container);
	}

	
	@Override
	public IWizardPage getNextPage() {
		items.clear();
		List<Item> selectedItems = new ArrayList<>();
		for (Object selection : viewer.getCheckedElements()) {
			Item item = (Item) selection;
			selectedItems.add(item);
		}
		items.addAll(selectedItems);
		return super.getNextPage();
	}

}
