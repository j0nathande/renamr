package de.j0nathan.renamr;

import java.io.File;
import java.util.List;

import de.j0nathan.renamr.vo.ReplaceInstruction;

/**
 * Utility class for common methods.
 * 
 * @author j0nathan
 *
 */
public class Utils {
	
	/**
	 * Returns given text with upper first char.
	 * 
	 * @param text Text to transform.
	 * @return Text with upper first char.
	 */
	public String toUpperCaseFirstChar(String text) {
		return Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}
	
	/**
	 * Returns given text with lower first char.
	 * 
	 * @param text Text to transform.
	 * @return Text with lower first char.
	 */
	public String toLowerCaseFirstChar(String text) {
		return Character.toLowerCase(text.charAt(0)) + text.substring(1);
	}

	/**
	 * Checks if given file is a folder and exists.
	 * 
	 * @param file The file to check.
	 * @throws IllegalArgumentException If file is not a folder or does not exist.
	 */
	public void assertFolderExists(File file) throws IllegalArgumentException {
		if (file == null || !file.exists() || !file.isDirectory()) {
			throw new IllegalArgumentException("Folder " + file + " is not valid. It must exist.");
		}
	}
	
	/**
	 * Checks if given file doesn't exist.
	 * 
	 * @param file The file to check.
	 * @throws IllegalArgumentException If file exists.
	 */
	public void assertFolderNotExists(File file) throws IllegalArgumentException {
		if (file == null || file.exists()) {
			throw new IllegalArgumentException("Folder " + file + " is not valid. It must not exist.");
		}
	}

	/**
	 * Modifies given text by search and replace strings of ReplaceInstruction-Objects.
	 * 
	 * @param text The text to modify.
	 * @param replaceInstructionList The instructions to modify the text.
	 * @return The modified text.
	 */
	public String modifyText(String text, final List<ReplaceInstruction> replaceInstructionList) {
		// Iterate over all replace Instructions:
		for(ReplaceInstruction replaceInstruction : replaceInstructionList) {
			if (text.contains(replaceInstruction.getSearch())) {
				text = text.replaceAll(replaceInstruction.getSearch(), replaceInstruction.getReplace());
			}
		}
		return text;
	}
	
}