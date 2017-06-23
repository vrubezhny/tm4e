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

import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.internal.themes.IThemeDescriptor;

/**
 * Label provider for Eclipse theme.
 */
public class EclipseThemeLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		ITheme theme = (ITheme) element;
		return theme.getLabel();
	}

}