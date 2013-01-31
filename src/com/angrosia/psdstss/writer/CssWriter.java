package com.angrosia.psdstss.writer;

import com.angrosia.psdstss.model.PsdFileContent.Slice;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CssWriter extends StylesheetWriter {
    private String basicFilename = "global.css";

    private Map<String, FileWriter> cssFiles = new HashMap<String, FileWriter>();
    private Pattern cssClassPattern = Pattern.compile("-?[_a-zA-Z]+[_a-zA-Z0-9-]*");

    private HtmlFileWriter htmlFileWriter = null;

    public CssWriter(String directory, String prefix) {
        super(directory, prefix);
    }

    private void writeCssFile(File file, String content) throws IOException {
        String filename = file.getName();
        FileWriter fileWriter;
        if (!cssFiles.containsKey(filename)) {
            fileWriter = new FileWriter(file.getCanonicalFile());
            cssFiles.put(filename, fileWriter);

        } else {
            fileWriter = cssFiles.get(filename);
        }

        fileWriter.write(content);
    }

    @Override
    public void writeSlice(Slice slice, String psdFile) throws Exception {
        Integer sliceWidth = slice.getRight() - slice.getLeft();
        Integer sliceHeight = slice.getBottom() - slice.getTop();

        String className = classPrefix + slice.getName().replaceAll("[^A-Za-z0-9-_]", "");
        Matcher classNameMatcher = cssClassPattern.matcher(className);
        if (!classNameMatcher.matches()) {
            throw new Exception("This classname seems to be broken: " + className);
        }

        String cssClass = "." + className + " {\r\n" +
                "    background-image: url('../" + FilenameUtils.getBaseName(psdFile) + ".png');\r\n" +
                "    background-position: -" + slice.getLeft() + "px -" + slice.getTop() + "px;\r\n" +
                "    height: " + sliceHeight + "px;\r\n" +
                "    width: " + sliceWidth + "px;\r\n" +
                "}\r\n\r\n";

        String outputFile = getOutputFilename(slice);

        if (verbose) {
            System.out.println("Write to " + outputFile + ": " + className);
        }
        
        writeCssFile(new File(outputPath + File.separator + outputFile), cssClass);

        if (htmlCreate) {
            if (verbose) {
                System.out.println("Write slice to HTML-File");
            }
            if (htmlFileWriter == null) {
                htmlFileWriter = new HtmlFileWriter(new File(outputPath + File.separator + "overview.html"));
            }
            htmlFileWriter.addSlice(outputFile, className, slice);
        }
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
        for (FileWriter fileWriter : cssFiles.values()) {
            fileWriter.flush();
            fileWriter.close();
        }

        if (htmlFileWriter != null) {
            htmlFileWriter.done();
        }
    }
}
