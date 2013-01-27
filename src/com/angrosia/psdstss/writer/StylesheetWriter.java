package com.angrosia.psdstss.writer;

import com.angrosia.psdstss.model.PsdFileContent.Slice;

import java.io.File;

public abstract class StylesheetWriter {
    protected File outputPath;
    protected String classPrefix;

    protected StylesheetWriter(File outputPath, String classPrefix) {
        this.outputPath = outputPath;
        this.classPrefix = classPrefix;
    }

    public abstract void writeSlice(Slice slice);
}
