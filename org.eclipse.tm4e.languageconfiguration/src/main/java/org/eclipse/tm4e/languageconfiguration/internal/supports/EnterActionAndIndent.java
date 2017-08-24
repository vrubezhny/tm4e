package org.eclipse.tm4e.languageconfiguration.internal.supports;

public class EnterActionAndIndent {

	private final EnterAction enterAction;

	private final String indentation;

	/**
	 * @param enterAction
	 * @param indentation
	 */
	public EnterActionAndIndent(EnterAction enterAction, String indentation) {
		super();
		this.enterAction = enterAction;
		this.indentation = indentation;
	}

	public EnterAction getEnterAction() {
		return enterAction;
	}

	public String getIndentation() {
		return indentation;
	}
}
