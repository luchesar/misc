package com.github.luchesar.shazam.wordcounter;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class WordCounterTest {
    private static final String LS = System.getProperty("line.separator");

    private WordCounter counter = new WordCounter();
    private ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
    private ByteArrayOutputStream errBytes = new ByteArrayOutputStream();
    private PrintStream out = new PrintStream(outBytes);
    private PrintStream err = new PrintStream(errBytes);

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void testCountFewSpaceDelimitedWords() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "this is a very simple \nfile content for counting\t   words");
        assertEquals(10, counter.count(new Config(sample.getAbsolutePath(), false)));

        WordCounter.count(new String[]{sample.getAbsolutePath()}, out, err);
        assertTrue("No errors are expected for the simple case", errBytes.toString().isEmpty());
        assertEquals("Should count the words correctly", 10, Integer.parseInt(outBytes.toString().trim()));
    }

    @Test
    public void testCountSingleWord() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "this");
        assertEquals(1, counter.count(new Config(sample.getAbsolutePath(), false)));

        WordCounter.count(new String[]{sample.getAbsolutePath()}, out, err);
        assertTrue("No errors are expected for the simple case", errBytes.toString().isEmpty());
        assertEquals("Should count the words correctly", 1, Integer.parseInt(outBytes.toString().trim()));
    }

    @Test
    public void testWordCountEmptyFile() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "");

        assertEquals(0, counter.count(new Config(sample.getAbsolutePath(), false)));

        WordCounter.count(new String[]{sample.getAbsolutePath()}, out, err);
        assertTrue("No errors are expected for the simple case", errBytes.toString().isEmpty());
        assertEquals("Should count the words correctly", 0, Integer.parseInt(outBytes.toString().trim()));
    }

    @Test
    public void testWordCountFileWithWhiteSpaceOnly() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "   \n\n\t\n \n \t");

        assertEquals(0, counter.count(new Config(sample.getAbsolutePath(), false)));

        WordCounter.count(new String[]{sample.getAbsolutePath()}, out, err);
        assertTrue("No errors are expected for the simple case", errBytes.toString().isEmpty());
        assertEquals("Should count the words correctly", 0, Integer.parseInt(outBytes.toString().trim()));
    }

    @Test
    public void testSimpleLineCount() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "this is a simple\n\rline delimited\nfile\n\rcontent\n");

        assertEquals(4, counter.count(new Config(sample.getAbsolutePath(), true)));

        WordCounter.count(new String[]{sample.getAbsolutePath(), "-l"}, out, err);
        assertTrue("No errors are expected for the simple case", errBytes.toString().isEmpty());
        assertEquals("Should count the words correctly", 4, Integer.parseInt(outBytes.toString().trim()));
    }

    @Test
    public void testLineCountEmptyFile() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "");

        assertEquals(0, counter.count(new Config(sample.getAbsolutePath(), true)));

        WordCounter.count(new String[]{sample.getAbsolutePath(), "-l"}, out, err);
        assertTrue("No errors are expected for the simple case", errBytes.toString().isEmpty());
        assertEquals("Should count the words correctly", 0, Integer.parseInt(outBytes.toString().trim()));
    }

    @Test
    public void testLineCountFileWithWhiteSpaceOnly() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "   \n\n\t\n \n \t");

        assertEquals(4, counter.count(new Config(sample.getAbsolutePath(), true)));

        WordCounter.count(new String[]{sample.getAbsolutePath(), "-l"}, out, err);
        assertTrue("No errors are expected for the simple case", errBytes.toString().isEmpty());
        assertEquals("Should count the words correctly", 4, Integer.parseInt(outBytes.toString().trim()));
    }

    @Test
    public void testWordCountMissingFile() throws Exception {
        try {
            counter.count(new Config("some/missing/file/path", false));
            fail("IllegalArgumentException expected when passing missing file");
        } catch (IllegalArgumentException e) {
            assertEquals("File 'some/missing/file/path' does not exist!", e.getMessage());
        }
        try {
            WordCounter.count(new String[]{"some/missing/file/path"}, out, err);
            fail("IllegalArgumentException expected when passing missing file");
        } catch (IllegalArgumentException e) {
            // works as expected
        }
        assertTrue(outBytes.toString().isEmpty());
        assertEquals("Any error should be printed to the system.err",
                "Cannot perform counting" + LS + "File 'some/missing/file/path' does not exist!" + LS, errBytes.toString());
    }

    @Test
    public void testLineCountMissingFile() throws Exception {
        try {
            counter.count(new Config("some/missing/file/path", true));
            fail("IllegalArgumentException expected when passing missing file");
        } catch (IllegalArgumentException e) {
            assertEquals("File 'some/missing/file/path' does not exist!", e.getMessage());
        }

        try {
            WordCounter.count(new String[]{"some/missing/file/path", "-l"}, out, err);
            fail("IllegalArgumentException expected when passing missing file");
        } catch (IllegalArgumentException e) {
            // works as expected
        }
        assertTrue(outBytes.toString().isEmpty());
        assertEquals("Any error should be printed to the system.err",
                "Cannot perform counting" + LS + "File 'some/missing/file/path' does not exist!" +LS, errBytes.toString());
    }

    @Test
    public void testNoArguments() throws Exception {
        try {
            counter.count(new Config(null, false));
            fail("No arguments should print simple help message");
        } catch (IllegalArgumentException e) {
            assertEquals("Please specify file path as a first argument to count the words or lines.", e.getMessage());
        }

        try {
            WordCounter.count(new String[]{}, out, err);
            fail("No arguments should print simple help message");
        } catch (IllegalArgumentException e) {
            // works as expected
        }
        assertTrue(outBytes.toString().isEmpty());
        assertEquals("Any error should be printed to the system.err",
                "Cannot perform counting" + LS + "Please specify file path as a first argument to count the words or lines." + LS,
                errBytes.toString());
    }

    @Test
    public void testCountWordsFromFewBigFiles() throws Exception {
        // Compare with the output of wc -w
        assertEquals(29461, counter.count(new Config(getFilePath("/alice.txt"), false)));
        assertEquals(71611, counter.count(new Config(getFilePath("/island.txt"), false)));
        assertEquals(58172, counter.count(new Config(getFilePath("/miserables.txt"), false)));
    }

    @Test
    public void testCountLinesFromFewBigFiles() throws Exception {
        // compare with the output of wc -l
        assertEquals(3735, counter.count(new Config(getFilePath("/alice.txt"), true)));
        assertEquals(7886, counter.count(new Config(getFilePath("/island.txt"), true)));
        assertEquals(6744, counter.count(new Config(getFilePath("/miserables.txt"), true)));
    }

    private String getFilePath(String classPath) throws Exception {
        return getClass().getResource(classPath).getFile();
    }
  
}
