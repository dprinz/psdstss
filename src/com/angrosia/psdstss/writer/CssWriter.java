package com.angrosia.psdstss.writer;

import com.angrosia.psdstss.model.PsdFileContent.Slice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CssWriter extends StylesheetWriter {
    private String basicFilename = "global.css";

    private Map<String, FileWriter> lessFiles = new HashMap<String, FileWriter>();
    private Pattern cssClassPattern = Pattern.compile("-?[_a-zA-Z]+[_a-zA-Z0-9-]*");

    public CssWriter(String directory, String prefix) {
        super(directory, prefix);
    }

    private void writeCssFile(File file, String content) throws IOException {
        String filename = file.getName();
        FileWriter fileWriter;
        if (!lessFiles.containsKey(filename)) {
            fileWriter = new FileWriter(file.getCanonicalFile());
            lessFiles.put(filename, fileWriter);

        } else {
            fileWriter = lessFiles.get(filename);
        }

        fileWriter.write(content);
    }

    @Override
    public void writeSlice(Slice slice) throws Exception {
        Integer sliceWidth = slice.getRight() - slice.getLeft();
        Integer sliceHeight = slice.getBottom() - slice.getTop();

        String className = classPrefix + slice.getName().replaceAll("[^A-Za-z0-9-_]", "");
        Matcher classNameMatcher = cssClassPattern.matcher(className);
        if (!classNameMatcher.matches()) {
            throw new Exception("This classname seems to be broken: " + className);
        }

        String cssClass = "." + className + " {\r\n" +
                "    background-image: url('/images/sprite.png');\r\n" +
                "    background-position: -" + slice.getLeft() + "px -" + slice.getTop() + "px;\r\n" +
                "    height: " + sliceHeight + "px;\r\n" +
                "    width: " + sliceWidth + "px;\r\n" +
                "}\r\n\r\n";

        String outputFile = getOutputFilename(slice);

        if (verbose) {
            System.out.println("Write to " + outputFile + ": " + className);
        }

        writeCssFile(new File(outputPath + File.separator + outputFile), cssClass);
    }

    private String getOutputFilename(Slice slice) {
        String outputFile = slice.getTarget().trim();
        if (outputFile.isEmpty()) {
            outputFile = basicFilename;
        } else if(!outputFile.endsWith(".css")) {
            outputFile += ".css";
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
