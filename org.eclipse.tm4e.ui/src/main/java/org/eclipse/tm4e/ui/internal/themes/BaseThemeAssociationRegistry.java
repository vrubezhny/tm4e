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
		String eclipseThemeId = association.getEclipseThemeId();
		if (association.isDefault()) {
			if (eclipseThemeId == null) {
				defaultAssociation = association;
			} else {
				eclipseThemeIds.put(eclipseThemeId, association);
			}
		}
		allAssociations.add(association);
	}

	public void unregister(IThemeAssociation association) {
		String eclipseThemeId = association.getEclipseThemeId();
		if (association.isDefault()) {
			if (eclipseThemeId == null) {
				defaultAssociation = null;
			} else {
				eclipseThemeIds.remove(eclipseThemeId);
			}
		}
		allAssociations.remove(association);
	}

	public IThemeAssociation getThemeAssociationFor(String eclipseThemeId) {
		return eclipseThemeIds.get(eclipseThemeId);
	}

	public IThemeAssociation[] getThemeAssociations(boolean isDefault) {
		if (isDefault) {
			return allAssociations.stream().filter(theme -> theme.isDefault()).collect(Collectors.toList())
					.toArray(new IThemeAssociation[0]);
		}
		return allAssociations.toArray(new IThemeAssociation[allAssociations.size()]);
	}

	public IThemeAssociation getDefaultAssociation() {
		return defaultAssociation;
	}

	public IThemeAssociation[] getThemeAssociationsForTheme(String themeId) {
		return allAssociations.stream().filter(themeAssociation -> themeId.equals(themeAssociation.getThemeId()))
				.collect(Collectors.toList()).toArray(new IThemeAssociation[0]);
	}
}
