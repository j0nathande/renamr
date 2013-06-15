package de.j0nathan.renamr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.j0nathan.renamr.vo.ReplaceInstruction;

/**
 * App should be called with following parameters: sourceFolder destinationFolder search1->replace1 search2->replace2 ...
 * 
 */
public class App {
	
	static File sourceFolder;
	static File destinationFolder;
	static List<ReplaceInstruction> replaceInstructionsList = new ArrayList<ReplaceInstruction>();
	
	private static Logger LOG = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) throws IOException {
		assertValidParameters(args);
		
		FileCopy fc = new FileCopy(sourceFolder, destinationFolder);
		File destinationFolder2 = fc.copyToDestinationFolderAndReplaceInFilenames(replaceInstructionsList);
		
		FileModify fm = new FileModify(destinationFolder2);
		fm.replace(replaceInstructionsList);
	}

	private static void assertValidParameters(String[] args) {
		if (args.length < 3) {
			throw new IllegalArgumentException("Invalid parameters. App should be called with following parameters: sourceFolder destinationFolder search1->replace1 search2->replace2 ...");
		}
		sourceFolder = new File(args[0]);
		destinationFolder = new File(args[1]);
		
		for (int i = 2; i < args.length; i++) {
			ReplaceInstruction replaceInstruction = constructFromString(args[i]);
			replaceInstructionsList.add(replaceInstruction);
		}
		
		LOG.info("App called with parameters ["
				+ "\n   sourceFolder=" + sourceFolder
				+ ",\n   destinationFolder=" + destinationFolder
				+ ",\n   replaceInstructions="
				+ "\n" + getReplaceInstructionsListAsString()
				+"]");
		
		if (destinationFolder.exists()) {
			throw new IllegalArgumentException("Destination folder allready exists. Doing nothing.");
		}
	}

	private static String getReplaceInstructionsListAsString() {
		StringBuffer sb = new StringBuffer();
		int i = 1;
		for (ReplaceInstruction ri : replaceInstructionsList) {
			sb.append("      ");
			sb.append(i++);
			sb.append(". ");
			sb.append(ri);
			sb.append("\n");
		}
		return sb.toString();
	}

	private static ReplaceInstruction constructFromString(String text) {
		String[] split = text.split("->");
		if (split.length != 2) {
			throw new IllegalArgumentException("Search and replace string is wrong. Should be search->replace. Doing nothing.");
		}
		return new ReplaceInstruction(split[0], split[1]);
	}
}
