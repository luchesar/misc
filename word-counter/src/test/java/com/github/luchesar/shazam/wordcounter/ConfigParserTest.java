package com.github.luchesar.shazam.wordcounter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigParserTest {
    private ConfigParser parser = new ConfigParser();

    @Test
    public void parseNoArguments() throws Exception {
        assertEquals(new Config(null, false), parser.parse(new String[0]));
    }

    @Test
    public void parseSingleArgumentWithNoDash() throws Exception {
        assertEquals(new Config("noDashArgument", false), parser.parse(new String[]{"noDashArgument"}));
    }

    @Test
    public void parseMultipleArguments() throws Exception {
        assertEquals(new Config("argument1", false), parser.parse(new String[]{"argument1", "argument2"}));
    }

    @Test
    public void parseTwoValidArguments() throws Exception {
        assertEquals(new Config("argument1", true), parser.parse(new String[]{"argument1", "-l"}));
        assertEquals(new Config("filePath", true), parser.parse(new String[]{"-l", "filePath"}));
    }

    @Test
    public void parseValidAndInvalidArguments() throws Exception {
        assertEquals(new Config("filePath", true), parser.parse(new String[]{"-l", "filePath", "argument2", "-m"}));
        assertEquals(new Config("filePath", false), parser.parse(new String[]{"filePath", "argument2", "-m"}));
    }
}
