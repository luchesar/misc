package com.github.luchesar.shazam.wordcounter;

import java.io.*;

public class WordCounter {

    public int count(Config config) throws IOException {
        if (config.getFilePath() == null) {
            throw new IllegalArgumentException("Please specify file path as a first argument to count the words or lines.");
        }
        File file = new File(config.getFilePath());
        if (!file.exists()) {
            throw new IllegalArgumentException("File '" + config.getFilePath() + "' does not exist!");
        }
        if (config.isCountLines()) {
            return countLines(file);
        }
        return countWords(file);
    }

    private int countWords(File file) throws IOException {
        try (InputStreamReader in = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
            int counter = 0;
            int r;
            boolean inWhiteSpace = true;
            while ((r = in.read()) != -1) {
                char ch = (char) r;
                if (Character.isWhitespace(ch)) {
                    inWhiteSpace = true;
                } else if (inWhiteSpace) {
                    counter += 1;
                    inWhiteSpace = false;
                }
            }

            return counter;
        }
    }

    private int countLines(File file) throws IOException {
        try (InputStreamReader in = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
            int counter = 0;
            int r;
            while ((r = in.read()) != -1) {
                char ch = (char) r;
                switch (ch) {
                    case '\n':
                        counter += 1;
                }
            }

            return counter;
        }
    }

    public static void count(String[] args, PrintStream out, PrintStream err) throws Exception {
        try {
            Config config = new ConfigParser().parse(args);
            int result = new WordCounter().count(config);
            out.println(result);
        } catch (Exception e) {
            err.println("Cannot perform counting");
            err.println(e.getMessage());
            throw e;
        }
    }

    public static void main(String args[]) {
        try {
            WordCounter.count(args, System.out, System.err);
        } catch (Exception e) {
            System.exit(1);
        }
    }
}
