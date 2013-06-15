package de.j0nathan.renamr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.j0nathan.renamr.vo.ReplaceInstruction;

/**
 * This class offers methods to recursively copy files and folders from a sourceFolder to newly created destinationFolder and modify its filenames.<br>
 * 
 * @author jonathan
 *
 */
public class FileCopy {

	private static Logger LOG = LoggerFactory.getLogger(FileCopy.class);

	private File sourceFolder;
	private File destinationFolder;
	
	private static final String[] IGNORE_FOLDERS = new String[]{".hg", ".git", "target"};

	private Utils utils = new Utils();
	
	/**
	 * Private Constructor for not allowing to use default constructor.
	 */
	@SuppressWarnings("unused")
	private FileCopy() {
		// use FileCopy(File sourceFolder, File destinationFolder)
	}
	
	/**
	 * Initialize the source folder and the destination folder.
	 * 
	 * @param sourceFolder Folder to read from. Must exist.
	 * @param destinationFolder Folder to write to. Must not exist.
	 */
	public FileCopy(File sourceFolder, File destinationFolder) {
		utils.assertFolderExists(sourceFolder);
		utils.assertFolderNotExists(destinationFolder);
		this.sourceFolder = sourceFolder;
		this.destinationFolder = destinationFolder;
	}

	/**
	 * Recursively copy files and folders from set sourceFolder to newly created destinationFolder.<br>
	 * Folders specified in local field IGNORE_FOLDERS are being disregarded.
	 * No replacement is done.
	 * 
	 * @return The file reference to the destinationFolder.
	 * @throws IOException If something goes wrong.
	 */
	public File copyToDestinationFolderAndReplaceInFilenames() throws IOException {
		List<ReplaceInstruction> replaceInstructionList = new ArrayList<ReplaceInstruction>();
		return copyFolder(sourceFolder, destinationFolder, false, replaceInstructionList);
	}
	
	/**
	 * Recursively copy files and folders from set sourceFolder to newly created destinationFolder.<br>
	 * Folders specified in local field IGNORE_FOLDERS are being disregarded.
	 * Only the given ReplaceInstruction is processed.
	 * 
	 * @param replaceInstruction One ReplaceInstruction.
	 * @return The file reference to the destinationFolder.
	 * @throws IOException If something goes wrong.
	 */
	public File copyToDestinationFolderAndReplaceInFilenames(ReplaceInstruction replaceInstruction) throws IOException {
		List<ReplaceInstruction> replaceInstructionList = new ArrayList<ReplaceInstruction>();
		replaceInstructionList.add(replaceInstruction);
		return copyFolder(sourceFolder, destinationFolder, false, replaceInstructionList);
	}
	
	/**
	 * Recursively copy files and folders from set sourceFolder to newly created destinationFolder.<br>
	 * Folders specified in local field IGNORE_FOLDERS are being disregarded.
	 * All given ReplaceInstructions in the List are processed.
	 * 
	 * @param replaceInstructionList A list of ReplaceInstructions.
	 * @return The file reference to the destinationFolder.
	 * @throws IOException If something goes wrong.
	 */
	public File copyToDestinationFolderAndReplaceInFilenames(List<ReplaceInstruction> replaceInstructionList) throws IOException {
		return copyFolder(sourceFolder, destinationFolder, false, replaceInstructionList);
	}

	private File copyFolder(File src, File dest, boolean renameFileNames, List<ReplaceInstruction> replaceInstructionList) throws IOException {
		if (renameFileNames) {
			String filename = dest.getName();
			String modifiedFilename = utils.modifyText(filename, replaceInstructionList);
			if (filename != modifiedFilename) {
				dest = new File(dest.getParentFile(), modifiedFilename);
			}
		}
		if (src.isDirectory()) {
			LOG.debug("Folder copied from {} to {}.", src, dest);
			dest.mkdirs();
			// list all the directory contents
			String files[] = src.list();
			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				// check if folder is relevant
				if (!fileIsRelevant(srcFile)) {
					LOG.info("Ignoring folder {}.", srcFile);
					continue;
				}
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile, true, replaceInstructionList);
			}
		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
			LOG.debug("File copied from {} to {}.", src, dest);
		}
		return dest;
	}

	private boolean fileIsRelevant(File file) {
		if (file.isDirectory() && Arrays.asList(IGNORE_FOLDERS).contains(file.getName())) {
			return false;
		}
		return true;
	}

}
