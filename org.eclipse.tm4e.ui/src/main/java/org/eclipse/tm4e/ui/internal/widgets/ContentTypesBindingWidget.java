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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tm4e.ui.internal.TMUIMessages;

/**
 * Widget which displays content type list on the ledt and "New", "Remove"
 * buttons on the right.
 *
 */
public class ContentTypesBindingWidget extends TableAndButtonsWidget {

	public ContentTypesBindingWidget(Composite parent, int style) {
		super(parent, style, TMUIMessages.ContentTypesBindingWidget_description);
		super.setContentProvider(ArrayContentProvider.getInstance());
		super.setLabelProvider(new ContentTypeLabelProvider());
	}
}
