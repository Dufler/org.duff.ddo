package org.duff.ddo.gui.table;

import java.util.List;

import org.duff.ddo.crafting.data.PropertyDAO;
import org.duff.ddo.crafting.model.Item;
import org.duff.ddo.crafting.model.Property;
import org.eclipse.jface.viewers.CheckboxTableViewer;

public class ComboPrefixPropertyEditSupport extends ComboPropertyEditSupport {

	public ComboPrefixPropertyEditSupport(CheckboxTableViewer viewer) {
		super(viewer);
	}

	@Override
	protected List<Property> getAvaibleProperties(Item item) {
		return PropertyDAO.getInstance().getAvaiblePropertiesForPrefix(item);
	}

	@Override
	protected Property getProperty(Item item) {
		return item.getPrefix();
	}

	@Override
	protected void setProperty(Item item, Property property) {
		item.setPrefix(property);		
	}

}
