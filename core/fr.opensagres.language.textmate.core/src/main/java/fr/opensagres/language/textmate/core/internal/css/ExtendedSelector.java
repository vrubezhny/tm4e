package fr.opensagres.language.textmate.core.internal.css;

import java.util.List;

import org.w3c.css.sac.Selector;

public interface ExtendedSelector extends Selector {

	/**
	 * Returns the specificity of this selector.
	 */
	int getSpecificity();

	int nbMatch(List<String> names);
	
	int nbClass();
}
