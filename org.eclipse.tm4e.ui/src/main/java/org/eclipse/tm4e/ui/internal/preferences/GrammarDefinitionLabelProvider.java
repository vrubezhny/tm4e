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
import org.eclipse.tm4e.registry.IGrammarDefinition;

/**
 * Label provider for grammar definition.
 */
class GrammarDefinitionLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		IGrammarDefinition definition = (IGrammarDefinition) element;

		switch (columnIndex) {
		case 0:
			return definition.getScopeName();
		case 1:
			return definition.getPath();
		case 2:
			return definition.getPluginId();			
		default:
			return ""; //$NON-NLS-1$
		}
	}
}