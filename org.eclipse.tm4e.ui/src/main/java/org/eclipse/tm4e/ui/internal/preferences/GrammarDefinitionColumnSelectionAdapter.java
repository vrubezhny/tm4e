package org.eclipse.tm4e.ui.internal.preferences;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

class GrammarDefinitionColumnSelectionAdapter extends SelectionAdapter {

	private final TableColumn fTableColumn;
	private final TableViewer tableViewer;
	private final int fColumnIndex;
	private final GrammarDefinitionViewerComparator viewerComparator;

	public GrammarDefinitionColumnSelectionAdapter(TableColumn column, TableViewer tableViewer, int index,
			GrammarDefinitionViewerComparator vc) {
		fTableColumn = column;
		this.tableViewer = tableViewer;
		fColumnIndex = index;
		viewerComparator = vc;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		viewerComparator.setColumn(fColumnIndex);
		int dir = viewerComparator.getDirection();
		Table table = tableViewer.getTable();
		table.setSortDirection(dir);
		table.setSortColumn(fTableColumn);
		tableViewer.refresh();
	}
}
