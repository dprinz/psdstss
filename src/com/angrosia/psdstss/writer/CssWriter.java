package com.angrosia.psdstss.writer;

import com.angrosia.psdstss.model.PsdFileContent.Slice;

import java.io.File;

public class CssWriter extends StylesheetWriter {
    public CssWriter(File directory, String prefix) {
        super(directory, prefix);
    }

    @Override
    public void writeSlice(Slice slice) {

    }
}
