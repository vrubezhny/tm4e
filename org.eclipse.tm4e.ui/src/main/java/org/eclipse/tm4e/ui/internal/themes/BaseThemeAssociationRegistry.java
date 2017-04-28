package org.eclipse.tm4e.ui.internal.themes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.tm4e.ui.themes.IThemeAssociation;

/**
 * Base Theme association registry.
 *
 */
public class BaseThemeAssociationRegistry {

	private IThemeAssociation defaultAssociation;
	private final Map<String, IThemeAssociation> eclipseThemeIds;

	public BaseThemeAssociationRegistry() {
		eclipseThemeIds = new HashMap<>();
	}

	public void register(IThemeAssociation association) {
		String eclipseThemeId = association.getEclipseThemeId();
		if (eclipseThemeId == null) {
			defaultAssociation = association;
		} else {
			eclipseThemeIds.put(eclipseThemeId, association);
		}
	}

	public void unregister(IThemeAssociation association) {
		String eclipseThemeId = association.getEclipseThemeId();
		if (eclipseThemeId == null) {
			defaultAssociation = null;
		} else {
			eclipseThemeIds.remove(eclipseThemeId);
		}
	}

	public IThemeAssociation getThemeAssociationFor(String eclipseThemeId) {
		return eclipseThemeIds.get(eclipseThemeId);
	}

	public IThemeAssociation[] getThemeAssociations() {
		List<IThemeAssociation> associations = new ArrayList<>();
		associations.add(getDefaultAssociation());
		associations.addAll(eclipseThemeIds.values());
		return associations.toArray(new IThemeAssociation[associations.size()]);
	}

	public IThemeAssociation getDefaultAssociation() {
		return defaultAssociation;
	}
}
