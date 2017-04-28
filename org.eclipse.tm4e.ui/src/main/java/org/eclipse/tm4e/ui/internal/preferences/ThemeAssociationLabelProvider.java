/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.ui.internal.preferences;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tm4e.ui.themes.IThemeAssociation;

/**
 * Label provider for TextMate theme association.
 */
class ThemeAssociationLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getText(Object element) {
		return getColumnText(element, 0);
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		IThemeAssociation association = (IThemeAssociation) element;
		switch (columnIndex) {
		case 0:
			if (association.getEclipseThemeId() == null) {
				return association.getTheme().getName();
			}
			return association.getTheme().getName() + " when '" + association.getEclipseThemeId() + "'";
		default:
			return ""; //$NON-NLS-1$
		}
	}
}