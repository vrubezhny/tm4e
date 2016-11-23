package org.eclipse.textmate4e.core.internal.css;

import java.util.List;

public interface ExtendedCondition {

	 /**
     * Returns the specificity of this condition.
     */
    int getSpecificity();

    int nbClass();
    
	int nbMatch(List<String> names);
}
