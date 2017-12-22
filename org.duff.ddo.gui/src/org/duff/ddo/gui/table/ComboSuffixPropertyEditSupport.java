package org.duff.ddo.gui.table;

import java.util.List;

import org.duff.ddo.crafting.data.PropertyDAO;
import org.duff.ddo.crafting.model.Item;
import org.duff.ddo.crafting.model.Property;
import org.eclipse.jface.viewers.CheckboxTableViewer;

public class ComboSuffixPropertyEditSupport extends ComboPropertyEditSupport {

	public ComboSuffixPropertyEditSupport(CheckboxTableViewer viewer) {
		super(viewer);
	}

	@Override
	protected List<Property> getAvaibleProperties(Item item) {
		return PropertyDAO.getInstance().getAvaiblePropertiesForSuffix(item);
	}

	@Override
	protected Property getProperty(Item item) {
		return item.getSuffix();
	}

	@Override
	protected void setProperty(Item item, Property property) {
		item.setSuffix(property);
	}

}
