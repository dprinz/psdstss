package com.angrosia.psdstss.writer;

import com.angrosia.psdstss.model.PsdFileContent.Slice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LessWriter extends StylesheetWriter{
    private boolean isFirstSlice = true;
    private Map<String, FileWriter> lessFiles = new HashMap<String, FileWriter>();

    Pattern cssClassPattern = Pattern.compile("-?[_a-zA-Z]+[_a-zA-Z0-9-]*");

    public LessWriter(File directory, String prefix) {
        super(directory, prefix);
    }

    private void writeLessFile(File file, String content) throws IOException {
        String filename = file.getName();
        FileWriter fileWriter;
        if (!lessFiles.containsKey(filename)) {
            fileWriter = new FileWriter(file);
            lessFiles.put(filename, fileWriter);
        } else {
            fileWriter = lessFiles.get(filename);
        }

        fileWriter.write(content);
    }

    @Override
    public void writeSlice(Slice slice) throws IOException {
        if (isFirstSlice) {
            isFirstSlice = false;
            writeLessFile(new File(outputPath.getParent() + File.separator + "basic.less"),
                ".sprite(@x, @y, @width, @height, @repeat: no-repeat) {\r\n" +
                "   background-repeat: @repeat;\r\n" +
                "   background-image: url('/assets/images/mwc-sprite.png');\r\n" +
                "   background-position: (@x * -1) (@y * -1);\r\n" +
                "   height: @height;\r\n" +
                "   width: @width;\r\n" +
                "   padding: 0;\r\n" +
                "}\r\n\r\n");
        }

//        String cssClass = ".mwc-sprite-" + slice.getName().replaceAll("[^A-Za-z0-9-_]", "") + " {\r\n" +
//                "  .sprite(" + slice.getLeft() + "px," + slice.getTop() + "px," + sliceWidth + "px," +
//                    sliceHeight + "px" + repeatOptions + ");" + "\r\n" +
//                "}\r\n\r\n";
//        writeLessFile(new File);
    }

    @Override
    public void done() throws IOException {
        for (FileWriter fileWriter : lessFiles.values()) {
            fileWriter.flush();
            fileWriter.close();
        }
    }
}
