package de.j0nathan.renamr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.j0nathan.renamr.vo.ReplaceInstruction;

/**
 * This class offers methods to modify the content of text files of a folder.<br>
 * The default encoding for reading and writing files is UTF-8. 
 * Files with the ending '.properties' are being encoded ISO-8859-1 (see http://docs.oracle.com/javase/6/docs/api/java/util/Properties.html)
 * 
 * @author jonathan
 *
 */
public class FileModify {

	private static Logger LOG = LoggerFactory.getLogger(FileModify.class);

	private static final String DEFAULT_ENCODING = "UTF-8";
	
	private File folder;
	
	private Utils utils = new Utils();

	/**
	 * Private Constructor for not allowing to use default constructor.
	 */
	@SuppressWarnings("unused")
	private FileModify() {
		// use FileModify(File folder)
	}
	
	/**
	 * Initialize the folder which contains the files to modify.
	 * 
	 * @param folder The folder which contains the files to modify.
	 */
	public FileModify(File folder) {
		utils.assertFolderExists(folder);
		this.folder = folder;
	}
	
	/**
	 * Start modifying the files.<br>
	 * The default encoding for reading and writing files is UTF-8. 
	 * Files with the ending '.properties' are being encoded ISO-8859-1 (see http://docs.oracle.com/javase/6/docs/api/java/util/Properties.html)
	 * 
	 * @param replaceInstruction The ReplaceInstruction to process.
	 * @throws IOException If something goes wrong.
	 */
	public void replace(ReplaceInstruction replaceInstruction) throws IOException {
		List<ReplaceInstruction> replaceInstructionList = new ArrayList<ReplaceInstruction>();
		replaceInstructionList.add(replaceInstruction);
		replace(folder, replaceInstructionList);
	}

	/**
	 * Start modifying the files.<br>
	 * The default encoding for reading and writing files is UTF-8. 
	 * Files with the ending '.properties' are being encoded ISO-8859-1 (see http://docs.oracle.com/javase/6/docs/api/java/util/Properties.html)
	 * 
	 * @param replaceInstructionList The ReplaceInstruction list to process.
	 * @throws IOException If something goes wrong.
	 */
	public void replace(List<ReplaceInstruction> replaceInstructionList) throws IOException {
		replace(folder, replaceInstructionList);
	}

	private void replace(File file, final List<ReplaceInstruction> replaceInstructionList)
			throws IOException {
		if (file.isDirectory()) {
			// list all the directory contents
			String files[] = file.list();
			for (String fileInFolder : files) {
				replace(new File(file, fileInFolder), replaceInstructionList);
			}
		} else {
			String text = readContent(file);
			String modifiedText = utils.modifyText(text, replaceInstructionList);
			if (text != modifiedText) {
				writeContent(file, modifiedText);
				LOG.info("File modified: {}.", file);
			} else {
				LOG.debug("File not modified: {}.", file);
			}
		}
	}
	
	protected String readContent(File file) throws IOException {
		// 0. Get correct encoding for file:
		String encoding = getEncodingForFilename(file.getName());
		
		// 1. Read file in correct encoding:
		BufferedReader reader  = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
		StringBuilder text = new StringBuilder();
	    String NL = System.getProperty("line.separator");
	    String line;
		while ((line = reader.readLine()) != null) {
			text.append(line + NL);
		}
		reader.close();
		
		return text.toString();
	}
	
	protected void writeContent(File file, String text) throws IOException {
		// 0. Get correct encoding for file:
		String encoding = getEncodingForFilename(file.getName());
		
		// 1. Write file in correct encoding:
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), encoding);
		BufferedWriter out = new BufferedWriter(outputStreamWriter);
        out.write(text);
        out.close();
	}
	
	private String getEncodingForFilename(String name) {
		if (name.endsWith(".properties")) {
			return "ISO-8859-1";
		}
		return DEFAULT_ENCODING;
	}

}
