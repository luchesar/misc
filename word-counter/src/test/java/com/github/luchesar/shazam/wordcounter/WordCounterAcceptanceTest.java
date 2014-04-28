package com.github.luchesar.shazam.wordcounter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class WordCounterAcceptanceTest {
    private static final String FAT_JAR_PATH = "fatJarPath";
    private static final String LS = System.getProperty("line.separator");
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void testCountFewSpaceDelimitedWords() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "this is a very simple \nfile content for counting\t   words");

        assertCommandOutput(sample.getAbsolutePath(), 0, "10" + LS, "");
    }

    @Test
    public void testWordCountEmptyFile() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "");

        assertCommandOutput(sample.getAbsolutePath(), 0, "0" + LS, "");
    }

    @Test
    public void testWordCountFileWithWhiteSpaceOnly() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "   \n\n\t\n \n \t");

        assertCommandOutput(sample.getAbsolutePath(), 0, "0" + LS, "");
    }

    @Test
    public void testSimpleLineCount() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "this is a simple\n\rline delimited\nfile\n\rcontent\n");

        assertCommandOutput(sample.getAbsolutePath() + " -l", 0, "4" + LS, "");
    }

    @Test
    public void testLineCountEmptyFile() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "");

        assertCommandOutput(sample.getAbsolutePath() + " -l", 0, "0" + LS, "");
    }

    @Test
    public void testLineCountFileWithWhiteSpaceOnly() throws Exception {
        File sample = tmpFolder.newFile("sample.txt");
        FileUtils.write(sample, "   \n\n\t\n \n \t");

        assertCommandOutput(sample.getAbsolutePath() + " -l", 0, "4" + LS, "");
    }

    @Test
    public void testWordCountMissingFile() throws Exception {
        assertCommandOutput("some/missing/file/path", 1, "", "Cannot perform counting" + LS
                +"File 'some/missing/file/path' does not exist!" + LS);
    }

    @Test
    public void testLineCountMissingFile() throws Exception {
        assertCommandOutput("some/missing/file/path -l", 1, "", "Cannot perform counting" + LS
                +"File 'some/missing/file/path' does not exist!" + LS);
    }

    @Test
    public void testNoArguments() throws Exception {
        assertCommandOutput("", 1, "", "Cannot perform counting" + LS
                +"Please specify file path as a first argument to count the words or lines." + LS);
    }

    @Test
    public void testCountWordsFromFewBigFiles() throws Exception {
        // Compare with the output of wc -w
        assertCommandOutput(getFilePath("/alice.txt"), 0, "29461" + LS, "");
        assertCommandOutput(getFilePath("/island.txt"), 0, "71611" + LS, "");
        assertCommandOutput(getFilePath("/miserables.txt"), 0, "58172" + LS, "");
    }

    @Test
    public void testCountLinesFromFewBigFiles() throws Exception {
        // compare with the output of wc -l
        assertCommandOutput(getFilePath("/alice.txt") + " -l", 0, "3735" + LS, "");
        assertCommandOutput(getFilePath("/island.txt") + " -l", 0, "7886" + LS, "");
        assertCommandOutput(getFilePath("/miserables.txt") + " -l", 0, "6744" + LS, "");
    }

    private String getFilePath(String classPath) throws Exception {
        return getClass().getResource(classPath).getFile();
    }

    private void assertCommandOutput(String args,
                                    int expectedErrCode,
                                    String expectedOut,
                                    String expectedErr) throws Exception {
        String java = System.getProperty("java.home") + "/bin/java";
        String exec = java + " -jar " + System.getProperty(FAT_JAR_PATH) + " " + args;
        Process p = Runtime.getRuntime().exec(exec);
        int errorCode = p.waitFor();
        assertEquals("From error code for '" + exec + "'", expectedErrCode, errorCode);
        assertEquals("From err for '" + exec + "'", expectedErr, IOUtils.toString(p.getErrorStream()));
        assertEquals("From output for '" + exec + "'", expectedOut, IOUtils.toString(p.getInputStream()));
    }
}
