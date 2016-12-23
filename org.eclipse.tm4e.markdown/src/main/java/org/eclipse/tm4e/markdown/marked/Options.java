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
