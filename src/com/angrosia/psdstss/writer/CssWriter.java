package com.angrosia.psdstss.writer;

import com.angrosia.psdstss.model.PsdFileContent.Slice;

import java.io.File;
import java.io.IOException;

public class CssWriter extends StylesheetWriter {
    public CssWriter(File directory, String prefix) {
        super(directory, prefix);
    }

    @Override
    public void writeSlice(Slice slice) {

    }

    @Override
    public void done() throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
