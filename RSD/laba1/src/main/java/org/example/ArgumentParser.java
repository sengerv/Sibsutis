package org.example;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class ArgumentParser {
    public static Arguments parse(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IOException("No arguments!");
        }

        List<String> files = new ArrayList<>();
        String PathResult = "./";
        String prefix = "-";
        boolean AddToExist = false;
        Statistics.StatMode statMode = Statistics.StatMode.NONE;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    PathResult = ErrorHandler(args, i, "Path not specified!");
                    i++;
                    break;
                case "-p":
                    prefix = ErrorHandler(args, i, "No prefix specified!");;
                    i++;
                    break;
                case "-a":
                    AddToExist = true;
                    break;
                case "-s":
                    statMode = checkStatMode(statMode, Statistics.StatMode.SHORT);
                    break;
                case "-f":
                    statMode = checkStatMode(statMode, Statistics.StatMode.FULL);
                    break;
                default:
                    files.add(args[i]);
            }
        }
        return new Arguments(files,PathResult,prefix,AddToExist,statMode);
    }

    public static String ErrorHandler(String[] args, int i, String message) throws IOException {
        if (i + 1 >= args.length) {
            throw new IOException(message);
        }
        return args[i + 1];
    }

    private static Statistics.StatMode checkStatMode(Statistics.StatMode currentMode, Statistics.StatMode newMode) {
        if (currentMode != Statistics.StatMode.NONE) {
            throw new IllegalArgumentException("Cannot be specified simultaneously -s and -f.");
        }
        return newMode;
    }
}
