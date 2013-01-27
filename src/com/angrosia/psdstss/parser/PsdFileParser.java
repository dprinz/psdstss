package com.angrosia.psdstss.parser;

import com.angrosia.psdstss.model.PsdFileContent.SlicesResource;
import com.angrosia.psdstss.parser.psdFileStructure.GenericSection;
import com.angrosia.psdstss.parser.psdFileStructure.ImageResources;

import java.io.*;

/**
 * @link http://www.adobe.com/devnet-apps/photoshop/fileformatashtml/PhotoshopFileFormats.htm
 */
public class PsdFileParser {
    private File psdFile;
    private GenericSection genericSection;
    private ImageResources imageResource;

    public PsdFileParser(File psdFile) {
        this.psdFile = psdFile;
        genericSection = new GenericSection();
        imageResource = new ImageResources();
    }

    public void registerSection(SlicesResource slicesResource) {
        imageResource.registerConsumer(slicesResource);
    }

    public void read() throws IOException {
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(this.psdFile));
        parse(stream);
        stream.close();
    }

    public void parse(InputStream inputStream) throws IOException {
        PsdInputStream stream = new PsdInputStream(inputStream);
        genericSection.skipHeaderSection(stream);
        genericSection.skipColorModeDataSection(stream);
        imageResource.parse(stream);
    }
}