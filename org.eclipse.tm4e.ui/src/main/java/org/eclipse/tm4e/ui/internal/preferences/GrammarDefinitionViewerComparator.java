package org.eclipse.tm4e.ui.internal.preferences;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

class GrammarDefinitionViewerComparator extends ViewerComparator {

	private int fSortColumn;

	private int fSortOrder; // 1 = asc, -1 = desc

	public GrammarDefinitionViewerComparator() {
		fSortColumn = 0;
		fSortOrder = 1;
	}

	/**
	 * Returns the {@linkplain SWT} style constant for the sort direction.
	 * 
	 * @return {@link SWT#DOWN} for asc sorting, {@link SWT#UP} otherwise
	 */
	public int getDirection() {
		return fSortOrder == 1 ? SWT.DOWN : SWT.UP;
	}

	/**
	 * Sets the sort column. If the newly set sort column equals the previous
	 * set sort column, the sort direction changes.
	 * 
	 * @param column
	 *            New sort column
	 */
	public void setColumn(int column) {
		if (column == fSortColumn) {
			fSortOrder *= -1;
		} else {
			fSortColumn = column;
			fSortOrder = 1;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		if (viewer instanceof TableViewer) {
			IBaseLabelProvider baseLabel = ((TableViewer) viewer).getLabelProvider();

			String left = ((GrammarDefinitionLabelProvider) baseLabel).getColumnText(e1, fSortColumn);
			String right = ((GrammarDefinitionLabelProvider) baseLabel).getColumnText(e2, fSortColumn);
			int sortResult = getComparator().compare(left, right);
			return sortResult * fSortOrder;
		}

		return super.compare(viewer, e1, e2);
	}

}
