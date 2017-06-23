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
package org.eclipse.tm4e.ui.internal.themes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.tm4e.ui.themes.IThemeAssociation;

/**
 * Base Theme association registry.
 *
 */
public class BaseThemeAssociationRegistry {

	private IThemeAssociation defaultAssociation;
	private final Map<String /* E4 Theme id */, IThemeAssociation> eclipseThemeIds;
	private List<IThemeAssociation> allAssociations;

	public BaseThemeAssociationRegistry() {
		eclipseThemeIds = new HashMap<>();
		this.allAssociations = new ArrayList<>();
	}

	public void register(IThemeAssociation association) {
		// when association is marked as default or scope name is defined,
		// update the default association or association for a given E4 Theme.
		if (association.isDefault() || association.getScopeName() != null) {
			String eclipseThemeId = association.getEclipseThemeId();
			if (eclipseThemeId == null) {
				defaultAssociation = association;
			} else {
				eclipseThemeIds.put(eclipseThemeId, association);
			}
		}
		allAssociations.clear();
	}

	public void unregister(IThemeAssociation association) {
		String eclipseThemeId = association.getEclipseThemeId();
		if (association.isDefault() || association.getScopeName() != null) {
			if (eclipseThemeId == null) {
				defaultAssociation = null;
			} else {
				eclipseThemeIds.remove(eclipseThemeId);
			}
		}
		allAssociations.clear();
	}

	public IThemeAssociation getThemeAssociationFor(String eclipseThemeId) {
		return eclipseThemeIds.get(eclipseThemeId);
	}

	public IThemeAssociation[] getThemeAssociations(boolean isDefault) {
		if (isDefault) {
			return getThemeAssociations().stream().filter(theme -> theme.isDefault()).collect(Collectors.toList())
					.toArray(new IThemeAssociation[0]);
		}
		return getThemeAssociations().toArray(new IThemeAssociation[allAssociations.size()]);
	}

	public IThemeAssociation getDefaultAssociation() {
		return defaultAssociation;
	}

	public IThemeAssociation[] getThemeAssociationsForTheme(String themeId) {
		return getThemeAssociations().stream().filter(themeAssociation -> themeId.equals(themeAssociation.getThemeId()))
				.collect(Collectors.toList()).toArray(new IThemeAssociation[0]);
	}

	public List<IThemeAssociation> getThemeAssociations() {
		if (allAssociations.isEmpty()) {
			if (defaultAssociation != null) {
				allAssociations.add(defaultAssociation);
			}
			allAssociations.addAll(eclipseThemeIds.values());
		}
		return allAssociations;
	}
}
