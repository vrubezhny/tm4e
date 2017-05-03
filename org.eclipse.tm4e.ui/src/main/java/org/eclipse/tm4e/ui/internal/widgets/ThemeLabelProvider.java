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
import org.eclipse.tm4e.ui.themes.ITheme;

/**
 * Label provider for TextMate theme.
 */
public class ThemeLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	
	@Override
	public String getText(Object element) {
		ITheme theme = (ITheme) element;
		return theme.getName();
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ITheme theme = (ITheme) element;
		switch (columnIndex) {
		case 0:
			return theme.getName();
		case 1:
			return theme.getPath();
		case 2:
			return theme.getPluginId();
		default:
			return ""; //$NON-NLS-1$
		}
	}
}