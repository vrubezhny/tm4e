package fr.opensagres.language.textmate.core.internal.css;

import java.util.List;

public interface ExtendedCondition {

	 /**
     * Returns the specificity of this condition.
     */
    int getSpecificity();

	boolean match(List<String> names);
}
