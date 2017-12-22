package org.duff.ddo.gui.table;

import java.util.List;

import org.duff.ddo.crafting.model.Item;
import org.duff.ddo.crafting.model.Property;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;

public abstract class ComboPropertyEditSupport extends EditingSupport {
	
	private final CheckboxTableViewer viewer;

	public ComboPropertyEditSupport(CheckboxTableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}
	
	protected abstract List<Property> getAvaibleProperties(Item item);
	
	protected abstract Property getProperty(Item item);
	
	protected abstract void setProperty(Item item, Property property);

	@Override
	protected CellEditor getCellEditor(Object element) {
		Item item = (Item) element;
		List<Property> avaibleProperties = getAvaibleProperties(item);
		String[] propertiesNames = new String[avaibleProperties.size()];
		for (int index = 0; index < avaibleProperties.size(); index++) {
			propertiesNames[index] = avaibleProperties.get(index).getName();
		}
		ComboBoxCellEditor editor = new ComboBoxCellEditor(viewer.getTable(), propertiesNames);
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		int index;
		Item item = (Item) element;
		Property prefix = getProperty(item);
		if (prefix != null) {
			List<Property> avaibleProperties = getAvaibleProperties(item);
			index = avaibleProperties.indexOf(prefix);
		} else {
			index = 0;
		}
		return index;
	}

	@Override
	protected void setValue(Object element, Object value) {
		Integer index = (Integer) value;
		if (index != null && index > 0) {
			Item item = (Item) element;
			List<Property> avaibleProperties = getAvaibleProperties(item);
			Property property = avaibleProperties.get(index);
			property.setPriority(11);
			setProperty(item, property);
			viewer.setChecked(element, true);
			viewer.refresh();
		}
	}

}
