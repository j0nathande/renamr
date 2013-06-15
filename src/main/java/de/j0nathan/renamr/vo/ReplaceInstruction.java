package de.j0nathan.renamr.vo;

public class ReplaceInstruction {

	private String search;
	private String replace;

	public ReplaceInstruction(String search, String replace) {
		this.search = search;
		this.replace = replace;
	}

	public String getSearch() {
		return search;
	}

	public String getReplace() {
		return replace;
	}
	
	
	public String toString() {
		return "Search '" + search + "' and replace with '" + replace + "'.";
	}

}
