package org.duff.ddo.gui.wizards;

import java.util.LinkedList;
import java.util.List;

import org.duff.ddo.crafting.model.Item;
import org.duff.ddo.crafting.model.Property;
import org.eclipse.jface.wizard.Wizard;

public class NewItemSetWizard extends Wizard {
	
	private final List<Item> items;
	private final List<Property> properties;
	
	private final SelectSlotWizardPage slotPage;
	private final SelectPropertyWizardPage propertyPage;
	private final ResultWizardPage resultPage;

	public NewItemSetWizard() {
		setWindowTitle("New Item Set - Wizard");
		items = new LinkedList<>();
		properties = new LinkedList<>();
		slotPage = new SelectSlotWizardPage(items);
		propertyPage = new SelectPropertyWizardPage(properties);
		resultPage = new ResultWizardPage(items, properties);
	}
	
	

	@Override
	public void addPages() {
		addPage(slotPage);
		addPage(propertyPage);
		addPage(resultPage);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
