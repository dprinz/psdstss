package com.angrosia.psdstss.writer;

import com.angrosia.psdstss.model.PsdFileContent.Slice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class LessWriter extends StylesheetWriter{
    private boolean isFirstSlice = true;
    private HashMap<String, File> lessFiles = new HashMap<String, File>();

    Pattern cssClassPattern = Pattern.compile("-?[_a-zA-Z]+[_a-zA-Z0-9-]*");

    public LessWriter(File directory, String prefix) {
        super(directory, prefix);
    }

    private void writeCssFile(File file, String classname) throws IOException {
        String filename = file.getName();
        if (!lessFiles.containsKey(filename)) {
            lessFiles.put(filename, file);
        }

        FileWriter fileWriter = new FileWriter(file);

        fileWriter.write(".sprite(@x, @y, @height, @width, @repeat: no-repeat) {\r\n" +
                "  background-image: url('');\r\n" +
                "}\r\n\r\n");

        fileWriter.flush();
        fileWriter.close();
    }

    @Override
    public void writeSlice(Slice slice) {
        if (isFirstSlice) {
            isFirstSlice = false;
        }
    }
}
