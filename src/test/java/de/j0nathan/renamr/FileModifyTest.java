package de.j0nathan.renamr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.j0nathan.renamr.vo.ReplaceInstruction;


/**
 * Unit tests for FileModify. Uses class FileCopy to setup tests.
 */
public class FileModifyTest {
	
	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	
	private File folder;
	private FileModify fm;
	
	@Before
	public void init() throws IOException {
		File sourceFolder = new File("src/test/resources/sourcetest");
		folder = new File(testFolder.getRoot(), "destinationfolder");
		FileCopy fc = new FileCopy(sourceFolder, folder);
		fc.copyToDestinationFolderAndReplaceInFilenames();
		fm = new FileModify(folder);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorWithInvalidFolderShouldThrowException() {
		File folder = new File("asdf");
		new FileModify(folder);
	}
	
	@Test
	public void modifyJavaShouldWork() throws IOException {
		fm.replace(new ReplaceInstruction("sourcetest", "modifytest"));
		File file = new File(folder.getPath() + "/test/Sourcetest.java");
		assertTrue(fileContainsString(file, "modifytest"));
		assertFalse(fileContainsString(file, "sourcetest"));
	}
	
	@Test
	public void modifyPropertiesShouldWork() throws IOException {
		fm.replace(new ReplaceInstruction("sourcetest", "modifytest"));
		File file = new File(folder.getPath() + "/sourcetest.properties");
		assertTrue(fileContainsString(file, "umlauttest=üöä\nproperty=modifytest"));
		assertFalse(fileContainsString(file, "sourcetest"));
	}
	
	@Test
	public void twoReplaceInstructionsShouldWork() throws IOException {
		List<ReplaceInstruction> replaceInstructionsList = new ArrayList<ReplaceInstruction>();
		replaceInstructionsList.add(new ReplaceInstruction("sourcetest", "modifytest"));
		replaceInstructionsList.add(new ReplaceInstruction("Sourcetest", "Modifytest"));
		fm.replace(replaceInstructionsList);
		File file = new File(folder.getPath() + "/test/Sourcetest.java");
		String modifiedFile = fm.readContent(file);
		String modifiedFileShouldBe = "package modifytest.test;\n\npublic class Modifytest {\n\n\t// modifytest\n\t\n}\n";
		assertEquals(modifiedFileShouldBe, modifiedFile);
	}
	
	private boolean fileContainsString(File file, String string) throws IOException {
		String oldtext = fm.readContent(file);
		return oldtext.contains(string);
	}
}
