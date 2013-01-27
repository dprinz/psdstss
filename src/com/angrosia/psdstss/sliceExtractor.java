package com.angrosia.psdstss;

import com.angrosia.psdstss.model.PsdFile;
import com.angrosia.psdstss.model.PsdFileContent.Slice;
import com.angrosia.psdstss.model.PsdFileContent.SlicesResource;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sliceExtractor {
    private static final byte INPUT_FILES = 0;
    private static final byte INPUT_DETECT = 1;
    private static final byte INPUT_FORMAT = 2;
    private static final byte INPUT_OUTPUT = 3;
    private static final byte INPUT_PREFIX = 4;

    private static final byte FORMAT_CSS = 0;
    private static final byte FORMAT_LESS = 1;

    private static byte format = FORMAT_LESS;
    private static String output = null;
    private static String prefix = "sprite-";
    private static boolean verbose = false;

    private static List<File> psdFiles = new ArrayList<File>();

    /**
     * @link http://stackoverflow.com/questions/1384947/java-find-txt-files-in-specified-folder
     */
    private static File[] finder(String dirName) {
        File dir = new File(dirName);
        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".psd");
            }
        });
    }

    public static void main(String[] args) throws Exception {
        setupHelp(args);
        setupParams(args);

        if (psdFiles.isEmpty()) {
            File[] fileScan = finder(System.getProperty("user.dir"));
            for (int i = 0; i < fileScan.length; i++) {
                psdFiles.add(fileScan[i]);
            }
        }

        for (File psdFile : psdFiles) {
            System.out.println("Parsing File: " + psdFile.getName());
            PsdFile psd = new PsdFile(psdFile);
            SlicesResource slicesResource = psd.getSliceResource();
            if (verbose) {
                System.out.println("  Size: " + (slicesResource.getRight() - slicesResource.getLeft()) +
                        " x " + (slicesResource.getBottom() - slicesResource.getTop()));
            }
            for (Slice slice : slicesResource.getSlices()) {
                if (slice.getGroupId() > 0 && !slice.getName().isEmpty()) {
                    System.out.println(slice.getName());
                    if (verbose) {
                        System.out.println("  Url:      " + slice.getUrl());
                        System.out.println("  Target:   " + slice.getTarget());
                        System.out.println("  Message:  " + slice.getMessage());
                        System.out.println("  AltTag:   " + slice.getAltTag());
                        System.out.println("  Position: " + slice.getLeft() + ", " + slice.getTop());
                        System.out.println("  Size:     " + (slice.getRight() - slice.getLeft()) +
                                " x " + (slice.getBottom() - slice.getTop()));
                    }
                }
            }
        }
    }

    private static void setupHelp(String[] args) {
        Pattern patternHelp = Pattern.compile("^[-]{1,}h(elp)*$");

        // help needed ?
        for (int i = 0; i < args.length; i++) {
            Matcher matcher = patternHelp.matcher(args[i]);
            if (matcher.matches()) {
                printHelp();
                System.exit(0);
            }
        }
    }

    private static void setupParams(String[] args) {
        Pattern patternInputFile = Pattern.compile("^[^-].+");
        Pattern patternFormat = Pattern.compile("^[-]{1,}f(ormat)*$");
        Pattern patternOutput = Pattern.compile("^[-]{1,}o(utput)*$");
        Pattern patternPrefix = Pattern.compile("^[-]{1,}p(refix)*$");
        Pattern patternVerbose = Pattern.compile("^[-]{1,}v(erbose)*$");
        Pattern patternCssClassname = Pattern.compile("-?[_a-zA-Z]+[_a-zA-Z0-9-]*");

        byte inputMode = INPUT_FILES;
        for (int i = 0; i < args.length; i++) {
            if (inputMode == INPUT_FILES) {
                Matcher matcher = patternInputFile.matcher(args[i]);
                if (matcher.matches()) {
                    psdFiles.add(new File(System.getProperty("user.dir") + File.separator + args[i]));
                } else {
                    inputMode = INPUT_DETECT;
                }
            }
            if (inputMode == INPUT_DETECT) {
                Matcher matcherVerbose = patternVerbose.matcher(args[i]);
                if (matcherVerbose.matches()) {
                    verbose = true;
                    continue;
                }
                Matcher matcherFormat = patternFormat.matcher(args[i]);
                if (matcherFormat.matches()) {
                    inputMode = INPUT_FORMAT;
                    continue;
                }
                Matcher matcherOutput = patternOutput.matcher(args[i]);
                if (matcherOutput.matches()) {
                    inputMode = INPUT_OUTPUT;
                    continue;
                }
                Matcher matcherPrefix = patternPrefix.matcher(args[i]);
                if (matcherPrefix.matches()) {
                    inputMode = INPUT_PREFIX;
                    continue;
                }
            }
            switch (inputMode) {
                case INPUT_FORMAT:
                    if (args[i].equals("css")) {
                        format = FORMAT_CSS;
                    } else if (args[i].equals("less")) {
                        format = FORMAT_LESS;
                    } else {
                        throw new IllegalArgumentException("Only 'css' and 'less' are known formats.");
                    }
                    break;
                case INPUT_OUTPUT:
                    output = args[i];
                    break;
                case INPUT_PREFIX:
                    Matcher matcherCssClassname = patternCssClassname.matcher(args[i]);
                    if (matcherCssClassname.matches()) {
                        prefix = args[i];
                    } else {
                        throw new IllegalArgumentException("This stylesheet class is not valid.");
                    }
                    break;
            }
            if (inputMode != INPUT_FILES) {
                inputMode = INPUT_DETECT;
            }
        }
    }

    private static void printHelp() {
        System.out.println(
            "psdstss 1.0.0 - (C) 2013 René Peschmann\r\n" +
            "Released under the Apache License, Version 2.0.\r\n\r\n" +
            "Extract slices from Photoshop psd to stylesheets.\r\n" +
            "Usage: psdstss [PSD-File]* [-f css|less] [-o OUTPUT-File] [-p class-prefix]\r\n" +
            "You can use more PSD-Files or leave it and let the script scan for all\r\n" +
            "PSD-Files in folder. From slices the <name> will be the <css-class>,\r\n" +
            "the <target> will be the <css-file>, in <alt-tags> are the options!\r\n" +
            "Alt-Tag-Options can be: repeat-x, repeat-y, repeat.\r\n\r\n" +
            "  -h, --help      Show this help.\r\n" +
            "  -f, --format    Set output format (css, less). Default: less\r\n" +
            "  -o, --output    The output file, if you don't want to let the script decide.\r\n" +
            "  -p, --prefix    Set the prefix of the classes. Default: sprite-\r\n" +
            "  -v, --verbose   Show information about files and parsing etc.\r\n\r\n" +
            "Example: psdstss sprite.psd -f less -p org-logo-"
        );
    }
}
