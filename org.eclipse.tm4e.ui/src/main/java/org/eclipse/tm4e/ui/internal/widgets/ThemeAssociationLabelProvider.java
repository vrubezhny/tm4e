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
package org.eclipse.tm4e.ui.internal.widgets;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tm4e.ui.TMUIPlugin;
import org.eclipse.tm4e.ui.themes.ITheme;
import org.eclipse.tm4e.ui.themes.IThemeAssociation;
import org.eclipse.tm4e.ui.themes.IThemeManager;

/**
 * Label provider for TextMate theme association.
 */
public class ThemeAssociationLabelProvider extends LabelProvider implements ITableLabelProvider {

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
			ITheme theme = getTheme(association);
			StringBuilder label = new StringBuilder(theme != null ? theme.getName() : association.getThemeId());
			if (association.isDefault()) {
				label.append(" (default)");
			}
			if (association.getEclipseThemeId() != null) {
				label.append(" when '");
				label.append(association.getEclipseThemeId());
				label.append("'");
			}
			return label.toString();
		default:
			return ""; //$NON-NLS-1$
		}
	}

	private ITheme getTheme(IThemeAssociation association) {
		String themeId = association.getThemeId();
		IThemeManager themeManager = TMUIPlugin.getThemeManager();
		return themeManager.getThemeById(themeId);
	}

}