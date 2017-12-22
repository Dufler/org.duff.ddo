package org.duff.ddo.gui.table;

import java.util.LinkedList;
import java.util.List;

import org.duff.ddo.crafting.ESlot;
import org.duff.ddo.crafting.model.Item;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class SlotTable {

	public static CheckboxTableViewer getSlotTable(Composite parent) {
		
		CheckboxTableViewer slotTable = CheckboxTableViewer.newCheckList(parent, SWT.BORDER | SWT.FULL_SELECTION);
		
		Table table = slotTable.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn columnName = new TableViewerColumn(slotTable, SWT.NONE);
		columnName.setLabelProvider(new ColumnLabelProvider() {
			@Override
		    public String getText(Object element) {
				Item item = (Item) element;
		        return item.getSlot().toString();
		    }
		});
		TableColumn tblclmnNome = columnName.getColumn();
		tblclmnNome.setWidth(100);
		tblclmnNome.setText("Slot");
		
		TableViewerColumn columnPrefix = new TableViewerColumn(slotTable, SWT.NONE);
		columnPrefix.setLabelProvider(new ColumnLabelProvider() {
			@Override
		    public String getText(Object element) {
				Item item = (Item) element;
				String testo;
				if (item != null && item.getPrefix() != null) {
					testo = item.getPrefix().getName();
				} else {
					testo = "-";
				}
				return testo;
		    }
		});
		columnPrefix.setEditingSupport(new ComboPrefixPropertyEditSupport(slotTable));
		TableColumn tblclmnPrefix = columnPrefix.getColumn();
		tblclmnPrefix.setWidth(200);
		tblclmnPrefix.setText("Prefix");
		
		TableViewerColumn columnSuffix = new TableViewerColumn(slotTable, SWT.NONE);
		columnSuffix.setLabelProvider(new ColumnLabelProvider() {
			@Override
		    public String getText(Object element) {
				Item item = (Item) element;
				String testo;
				if (item != null && item.getSuffix() != null) {
					testo = item.getSuffix().getName();
				} else {
					testo = "-";
				}
		        return testo;
		    }
		});
		columnSuffix.setEditingSupport(new ComboSuffixPropertyEditSupport(slotTable));
		TableColumn tblclmnSuffix = columnSuffix.getColumn();
		tblclmnSuffix.setWidth(200);
		tblclmnSuffix.setText("Suffix");
		
		TableViewerColumn columnExtra = new TableViewerColumn(slotTable, SWT.NONE);
		columnExtra.setLabelProvider(new ColumnLabelProvider() {
			@Override
		    public String getText(Object element) {
				Item item = (Item) element;
				String testo;
				if (item != null && item.getExtra() != null) {
					testo = item.getExtra().getName();
				} else {
					testo = "-";
				}
				return testo;
		    }
		});
		columnExtra.setEditingSupport(new ComboExtraPropertyEditSupport(slotTable));
		TableColumn tblclmnExtra = columnExtra.getColumn();
		tblclmnExtra.setWidth(200);
		tblclmnExtra.setText("Extra");

		slotTable.setContentProvider(ArrayContentProvider.getInstance());
		slotTable.setInput(getBaseItems());
		
		return slotTable;
	}
	
	private static List<Item> getBaseItems() {
		List<Item> baseItems = new LinkedList<>();
		for (ESlot slot : ESlot.values()) {
			Item item = new Item(slot);
			baseItems.add(item);
		}
		return baseItems;
	}

}
