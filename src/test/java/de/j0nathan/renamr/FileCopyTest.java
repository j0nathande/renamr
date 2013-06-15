package de.j0nathan.renamr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.j0nathan.renamr.vo.ReplaceInstruction;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Unit tests for FileCopy.
 */
public class FileCopyTest {
	
	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorWithInvalidSourceFolderShouldThrowException() {
		File sourceFolder = new File("asdf");
		File destinationFolder = new File("sourcetest");
		new FileCopy(sourceFolder, destinationFolder);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorWithExistingDestinationFolderShouldThrowException() {
		File sourceFolder = new File("src/test/resources/sourcetest");
		File destinationFolder = new File("src/test/resources/sourcetest");
		new FileCopy(sourceFolder, destinationFolder);
	}

	
	@Test
	public void onlyCopyShouldWork() throws IOException {
		File sourceFolder = new File("src/test/resources/sourcetest");
		File destinationFolder = new File(testFolder.getRoot(), "destinationfolder");
		FileCopy fc = new FileCopy(sourceFolder, destinationFolder);
		fc.copyToDestinationFolderAndReplaceInFilenames();
		assertThat(new File(destinationFolder.getPath() + "/test/Sourcetest.java").exists(), is(true));
	}
	
	@Test
	public void copyAndReplaceShouldWork() throws IOException {
		File sourceFolder = new File("src/test/resources/sourcetest");
		File destinationFolder = new File(testFolder.getRoot(), "destinationfolder");
		FileCopy fc = new FileCopy(sourceFolder, destinationFolder);
		fc.copyToDestinationFolderAndReplaceInFilenames(new ReplaceInstruction("test", "destinationtest"));
		// File /test/SourceTest.java -> /destinationtest/Sourcedestinationtest.java
		assertThat(new File(destinationFolder.getPath() + "/destinationtest/Sourcedestinationtest.java").exists(), is(true));
	}
	
	@Test
	public void foldersHgAndTargetShouldBeIgnored() throws IOException {
		File sourceFolder = new File("src/test/resources/sourcetest");
		File destinationFolder = new File(testFolder.getRoot(), "destinationfolder");
		FileCopy fc = new FileCopy(sourceFolder, destinationFolder);
		fc.copyToDestinationFolderAndReplaceInFilenames();
		assertThat(new File(destinationFolder.getPath() + "/.hg").exists(), is(false));
		assertThat(new File(destinationFolder.getPath() + "/target").exists(), is(false));
	}
	
	@Test
	public void emptyFolderShouldBeCopied() throws IOException {
		File sourceFolder = new File("src/test/resources/sourcetest");
		File destinationFolder = new File(testFolder.getRoot(), "destinationfolder");
		FileCopy fc = new FileCopy(sourceFolder, destinationFolder);
		fc.copyToDestinationFolderAndReplaceInFilenames();
		assertThat(new File(destinationFolder.getPath() + "/emptyfolder").exists(), is(true));
	}
	
	@Test
	public void searchTermShouldBeCaseSensitive() throws IOException {
		File sourceFolder = new File("src/test/resources/sourcetest");
		File destinationFolder = new File(testFolder.getRoot(), "destinationfolder");
		FileCopy fc = new FileCopy(sourceFolder, destinationFolder);
		fc.copyToDestinationFolderAndReplaceInFilenames(new ReplaceInstruction("tEst", "destinationtest"));
		// Nothing should be replaced. So /test/Sourcetest.java -> /test/Sourcetest.java
		assertThat(new File(destinationFolder.getPath() + "/test/Sourcetest.java").exists(), is(true));
	}
	
	@Test
	public void twoReplaceInstructionsShouldWork() throws IOException {
		File sourceFolder = new File("src/test/resources/sourcetest");
		File destinationFolder = new File(testFolder.getRoot(), "destinationfolder");
		FileCopy fc = new FileCopy(sourceFolder, destinationFolder);
		List<ReplaceInstruction> replaceInstructionsList = new ArrayList<ReplaceInstruction>();
		replaceInstructionsList.add(new ReplaceInstruction("Sourcetest", "Destinationtest"));
		replaceInstructionsList.add(new ReplaceInstruction("test", "destinationtest"));
		fc.copyToDestinationFolderAndReplaceInFilenames(replaceInstructionsList);
		// File /test/Sourcetest.java -> /destinationtest/Destinationdestinationtest.java
		assertThat(new File(destinationFolder.getPath() + "/destinationtest/Destinationdestinationtest.java").exists(), is(true));
	}
}
