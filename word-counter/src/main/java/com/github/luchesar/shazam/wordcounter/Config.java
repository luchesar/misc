package com.github.luchesar.shazam.wordcounter;

import java.util.Arrays;
import java.util.Objects;

class Config {
    private final String filePath;
    private final boolean isLineCount;

    Config(String filePath, boolean isLineCount) {
        this.filePath = filePath;
        this.isLineCount = isLineCount;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isCountLines() {
        return isLineCount;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Config)) {
            return false;
        }

        Config otherConfig = (Config)other;
        return Objects.equals(filePath, otherConfig.filePath) &&
                isLineCount == otherConfig.isLineCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, isLineCount);
    }

    @Override
    public String toString() {
        return Arrays.toString(new Object[]{filePath, isLineCount});
    }
}