package org.duff.ddo.gui.table;

import org.duff.ddo.crafting.model.Property;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;

public class PriorityEditingSupport extends EditingSupport {
	
	private final CheckboxTableViewer viewer;
    private final CellEditor editor;
	
	public PriorityEditingSupport(CheckboxTableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
        this.editor = new TextCellEditor(viewer.getTable());
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		Property property = (Property) element;
		String value = Integer.toString(property.getPriority()).toString();
		return value;
	}

	@Override
	protected void setValue(Object element, Object value) {
		Property property = (Property) element;
		try {
			int priority = Integer.parseInt(value.toString());
			if (priority < 1)
				priority = 1;
			if (priority > 10)
				priority = 10;
			property.setPriority(priority);
			viewer.setChecked(element, true);
		} catch (NumberFormatException e) {
			property.setPriority(0);
			viewer.setChecked(element, false);
		}
		viewer.refresh();
	}

}
