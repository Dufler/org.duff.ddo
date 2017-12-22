package org.duff.ddo.gui.dialogs;

import java.util.Set;

import org.duff.ddo.crafting.ESlot;
import org.duff.ddo.crafting.data.PropertyDAO;
import org.duff.ddo.crafting.model.Property;
import org.duff.ddo.crafting.model.Property.Group;
import org.duff.ddo.gui.composite.PropertySlotComposite;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PropertyDialog extends Dialog {
	
	private static final String title = "Property Editor";

	private Property property;

	private final boolean modify;

	private final PropertyDAO dao;

	private Text textName;
	private ComboViewer comboViewer;
	private Combo comboGroup;
	private PropertySlotComposite compositePrefix;
	private PropertySlotComposite compositeSuffix;
	private PropertySlotComposite compositeExtra;

	/**
	 * @wbp.parser.constructor
	 */
	public PropertyDialog() {
		super(Display.getDefault().getActiveShell());
		property = new Property();
		modify = false;
		dao = PropertyDAO.getInstance();
	}

	public PropertyDialog(Property property) {
		super(Display.getDefault().getActiveShell());
		this.property = property;
		modify = true;
		dao = PropertyDAO.getInstance();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
	}

	@Override
	protected Control createDialogArea(Composite container) {
		Composite parent = (Composite) super.createDialogArea(container);

		parent.setLayout(new GridLayout(1, false));

		Composite compositeInfo = new Composite(parent, SWT.NONE);
		compositeInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeInfo.setLayout(new GridLayout(2, false));

		Label lblName = new Label(compositeInfo, SWT.NONE);
		lblName.setText("Name: ");

		textName = new Text(compositeInfo, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblGroup = new Label(compositeInfo, SWT.NONE);
		lblGroup.setText("Group: ");

		comboViewer = new ComboViewer(compositeInfo, SWT.NONE);
		comboGroup = comboViewer.getCombo();
		comboGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setInput(Property.Group.values());

		compositePrefix = new PropertySlotComposite(parent, PropertySlotComposite.Type.PREFIX);
		compositePrefix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		compositeSuffix = new PropertySlotComposite(parent, PropertySlotComposite.Type.SUFFIX);
		compositeSuffix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		compositeExtra = new PropertySlotComposite(parent, PropertySlotComposite.Type.EXTRA);
		compositeExtra.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		if (modify)
			loadModel();
		
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		saveModel();
		if (modify) {
			dao.updateProperty(property);
		} else {
			dao.saveProperty(property);
		}
		super.okPressed();
	}

	private void loadModel() {
		textName.setText(property.getName());
		comboGroup.select(property.getGroup().getID());
		//Prefix
		Set<ESlot> prefixes = property.getPrefix();
		compositePrefix.setSelectedSlots(prefixes);
		//Suffix
		Set<ESlot> suffixes = property.getSuffix();
		compositeSuffix.setSelectedSlots(suffixes);
		//Extra
		Set<ESlot> extra = property.getExtra();
		compositeExtra.setSelectedSlots(extra);
	}

	private void saveModel() {
		property.setName(textName.getText());
		property.setGroup(Group.getByID(comboGroup.getSelectionIndex()));
		// Prefix
		property.resetPrefix();
		for (ESlot slot : compositePrefix.getSelectedSlots())
			property.addPrefix(slot);
		// Suffix
		property.resetSuffix();
		for (ESlot slot : compositeSuffix.getSelectedSlots())
			property.addSuffix(slot);
		// Extra
		property.resetExtra();
		for (ESlot slot : compositeExtra.getSelectedSlots())
			property.addExtra(slot);
	}

}
