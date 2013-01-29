package com.angrosia.psdstss.writer;

import com.angrosia.psdstss.model.PsdFileContent.Slice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LessWriter extends StylesheetWriter{
    private String basicFilename = "basic.less";

    private boolean isFirstSlice = true;
    private Map<String, FileWriter> lessFiles = new HashMap<String, FileWriter>();

    private Pattern cssClassPattern = Pattern.compile("-?[_a-zA-Z]+[_a-zA-Z0-9-]*");

    public LessWriter(String directory, String prefix) {
        super(directory, prefix);
    }

    private void writeLessFile(File file, String content, boolean needImport) throws IOException {
        String filename = file.getName();
        FileWriter fileWriter;
        if (!lessFiles.containsKey(filename)) {
            fileWriter = new FileWriter(file.getCanonicalFile());
            lessFiles.put(filename, fileWriter);
            if (needImport) {
                fileWriter.write("@import-once \"" + basicFilename + "\";\r\n\r\n");
            }
        } else {
            fileWriter = lessFiles.get(filename);
        }

        fileWriter.write(content);
    }

    @Override
    public void writeSlice(Slice slice) throws Exception {
        if (isFirstSlice) {
            isFirstSlice = false;
            writeLessFile(new File(outputPath + File.separator + basicFilename),
                ".sprite(@x, @y, @width, @height) {\r\n" +
                "   background-image: url('/images/sprite.png');\r\n" +
                "   background-position: (@x * -1) (@y * -1);\r\n" +
                "   height: @height;\r\n" +
                "   width: @width;\r\n" +
                "}\r\n\r\n", false);
        }

        Integer sliceWidth = slice.getRight() - slice.getLeft();
        Integer sliceHeight = slice.getBottom() - slice.getTop();

        String className = classPrefix + slice.getName().replaceAll("[^A-Za-z0-9-_]", "");
        Matcher classNameMatcher = cssClassPattern.matcher(className);
        if (!classNameMatcher.matches()) {
            throw new Exception("This classname seems to be broken: " + className);
        }

        String cssClass = "." + className + " {\r\n" +
                "  .sprite(" + slice.getLeft() + "px," + slice.getTop() + "px," +
                sliceWidth + "px," + sliceHeight + "px" + ");" + "\r\n" +
                "}\r\n\r\n";

        String outputFile = getOutputFilename(slice);

        if (verbose) {
            System.out.println("Write to " + outputFile + ": " + className);
        }

        writeLessFile(new File(outputPath + File.separator + outputFile), cssClass, true);
    }

    private String getOutputFilename(Slice slice) {
        String outputFile = slice.getTarget().trim();
        if (outputFile.isEmpty()) {
            outputFile = basicFilename;
        } else if(!outputFile.endsWith(".less")) {
            outputFile += ".less";
        }
        return outputFile;
    }

    @Override
    public void done() throws IOException {
        for (FileWriter fileWriter : lessFiles.values()) {
            fileWriter.flush();
            fileWriter.close();
        }
    }
}
