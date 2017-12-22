package org.duff.ddo.gui.table;

import java.util.List;

import org.duff.ddo.crafting.model.Item;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ItemTable extends TableViewer {
	
	private Table table;

	public ItemTable(Composite parent) {
		super(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION );
		
		table = getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn columnName = new TableViewerColumn(this, SWT.NONE);
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
		
		TableViewerColumn columnPrefix = new TableViewerColumn(this, SWT.NONE);
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
		TableColumn tblclmnPrefix = columnPrefix.getColumn();
		tblclmnPrefix.setWidth(100);
		tblclmnPrefix.setText("Prefix");
		
		TableViewerColumn columnSuffix = new TableViewerColumn(this, SWT.NONE);
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
		TableColumn tblclmnSuffix = columnSuffix.getColumn();
		tblclmnSuffix.setWidth(100);
		tblclmnSuffix.setText("Suffix");
		
		TableViewerColumn columnExtra = new TableViewerColumn(this, SWT.NONE);
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
		TableColumn tblclmnExtra = columnExtra.getColumn();
		tblclmnExtra.setWidth(100);
		tblclmnExtra.setText("Extra");

		setContentProvider(ArrayContentProvider.getInstance());
	}
	
	public void setContent(List<Item> items) {
		setInput(items);
		for (TableColumn tc : table.getColumns()) {
			tc.pack();
		}
	}

}
