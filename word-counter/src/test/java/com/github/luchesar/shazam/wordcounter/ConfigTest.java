package com.github.luchesar.shazam.wordcounter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ConfigTest {
    @Test
    public void testEqualsAndHashCode() throws Exception {
        Config c1 = new Config("file1", true);
        Config same = new Config("file1", true);
        Config differentIsLineCount = new Config("file1", false);
        Config differentFile = new Config("file2", false);
        Config different = new Config("file3", true);
        Config nullValue = new Config(null, true);

        assertEquals("Should be equal when same instance", c1, c1);
        assertFalse("Should not be equal to null", c1.equals(null));
        assertFalse("Should not be equal to random object", c1.equals(""));
        assertFalse("Should not be equal to random object", c1.equals(new Object()));

        assertEquals("Should be equal to object with the same values", c1, same);
        assertEquals("Should be equal to object with the same values", same, c1);
        assertFalse("Should not be equal to object wit different values", c1.equals(differentIsLineCount));
        assertFalse("Should not be equal to object wit different values", c1.equals(differentFile));
        assertFalse("Should not be equal to object wit different values", c1.equals(different));
        assertFalse("Should not be equal to object wit different values", c1.equals(nullValue));
    }
}
