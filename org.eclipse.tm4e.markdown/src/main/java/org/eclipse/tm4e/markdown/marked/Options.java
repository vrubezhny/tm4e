/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * This code is an translation of code copyrighted by https://github.com/chjj/marked, and initially licensed under MIT.
 *
 * Contributors:
 *  - https://github.com/chjj/marked: Initial code, written in JavaScript, licensed under MIT license
 *  - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.tm4e.markdown.marked;

public class Options {

	public static final Options DEFAULTS = new Options();

	private boolean gfm;
	private boolean breaks;
	private boolean pedantic;
	private boolean tables;

	public Options() {
		this.gfm = true;
	}

	public boolean isGfm() {
		return gfm;
	}

	public void setGfm(boolean gfm) {
		this.gfm = gfm;
	}

	public boolean isBreaks() {
		return breaks;
	}

	public void setBreaks(boolean breaks) {
		this.breaks = breaks;
	}

	public boolean isPedantic() {
		return pedantic;
	}

	public void setPedantic(boolean pedantic) {
		this.pedantic = pedantic;
	}

	public boolean isTables() {
		return tables;
	}

	public void setTables(boolean tables) {
		this.tables = tables;
	}

}
