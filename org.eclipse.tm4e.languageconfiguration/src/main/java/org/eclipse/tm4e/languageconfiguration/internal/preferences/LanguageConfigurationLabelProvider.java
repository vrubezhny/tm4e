/**
 *  Copyright (c) 2018 Red Hat Inc. and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Lucas Bullen (Red Hat Inc.) - initial API and implementation
 */
package org.eclipse.tm4e.languageconfiguration.internal.preferences;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tm4e.languageconfiguration.ILanguageConfigurationDefinition;

public class LanguageConfigurationLabelProvider extends LabelProvider implements ITableLabelProvider {

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
		ILanguageConfigurationDefinition definition = (ILanguageConfigurationDefinition) element;

		switch (columnIndex) {
		case 0:
			return definition.getContentType().getId();
		case 1:
			return definition.getPath();
		case 2:
			return definition.getPluginId();
		default:
			return ""; //$NON-NLS-1$
		}
	}
}