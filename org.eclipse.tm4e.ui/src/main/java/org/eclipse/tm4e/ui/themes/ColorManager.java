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
package org.eclipse.tm4e.ui.themes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tm4e.core.theme.RGB;

public class ColorManager {

	private static final ColorManager INSTANCE = new ColorManager();

	public static ColorManager getInstance() {
		return INSTANCE;
	}

	private final Map<RGB, Color> fColorTable;

	private ColorManager() {
		fColorTable = new HashMap<RGB, Color>(10);
	}

	public Color getColor(RGB rgb) {
		Color color = (Color) fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb.red, rgb.green, rgb.blue);
			fColorTable.put(rgb, color);
		}
		return color;
	}

	public void dispose() {
		Iterator<Color> e = fColorTable.values().iterator();
		while (e.hasNext()) {
			e.next().dispose();
		}
	}
}
