package com.github.luchesar.shazam.wordcounter;

class ConfigParser {
    public Config parse(String[] args) {
        String filePath = null;
        boolean isLineCount = false;

        for (String arg: args) {
            if (filePath != null && isLineCount) {
                break;
            }
            if (!arg.startsWith("-") && filePath == null) {
                filePath = arg;
            } else if ("-l".equals(arg)) {
                isLineCount = true;
            }
        }

        return new Config(filePath, isLineCount);
    }
}