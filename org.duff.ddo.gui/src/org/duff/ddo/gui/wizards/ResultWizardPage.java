package org.duff.ddo.gui.wizards;

import java.util.List;

import org.duff.ddo.crafting.logic.Optimizer;
import org.duff.ddo.crafting.model.Item;
import org.duff.ddo.crafting.model.Property;
import org.duff.ddo.gui.table.ItemTable;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ResultWizardPage extends WizardPage {

	private final List<Item> baseItems;
	private final List<Property> properties;
	//private final List<List<Item>> items;
	
	private ItemTable table;
	
	public ResultWizardPage(List<Item> baseItems, List<Property> properties) {
		super("wizardPage");
		setTitle("Find Optimal Setup");
		setDescription("Click 'Calculate' to find to optimal setup.");
		
		this.baseItems = baseItems;
		this.properties = properties;
		//this.items = new LinkedList<>();
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		Button btnCalculate = new Button(container, SWT.NONE);
		btnCalculate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				avviaProcessoCalcolo();
			}
		});
		btnCalculate.setText("Calculate");
		
		table = new ItemTable(container);
	}
	
	private void avviaProcessoCalcolo() {
		Optimizer optimizer = new Optimizer();
		List<Item> items = optimizer.findOptimum(baseItems, properties);
		table.setContent(items);
	}

}
