package com.angrosia.psdstss.model;

import com.angrosia.psdstss.model.PsdFileContent.SlicesResource;
import com.angrosia.psdstss.parser.PsdFileParser;

import java.io.File;
import java.io.IOException;

public class PsdFile {
    private SlicesResource slicesResource = new SlicesResource();

    public SlicesResource getSliceResource() {
        return slicesResource;
    }

    public PsdFile(File psdFile) throws IOException {
        PsdFileParser psdFileParser = new PsdFileParser(psdFile);
        psdFileParser.registerSection(slicesResource);
        psdFileParser.read();
    }
}
