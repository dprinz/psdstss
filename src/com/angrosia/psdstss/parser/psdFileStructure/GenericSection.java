package com.angrosia.psdstss.parser.psdFileStructure;

import com.angrosia.psdstss.parser.PsdInputStream;

import java.io.IOException;

public class GenericSection {
    private static final String PSD_SIGNATURE = "8BPS";
    private static final Short PSD_VERSION = 1;

    public void skipColorModeDataSection(PsdInputStream stream) throws IOException {
        Integer colorModeDataLength = stream.readInt();
        stream.skip(colorModeDataLength);
    }

    public void skipHeaderSection(PsdInputStream stream) throws IOException {
        String signature = stream.readAsciiString(4);
        if (!signature.equals(PSD_SIGNATURE)) {
            throw new IOException("File error: The signature of this file is not correct");
        }
        Short version = stream.readShort();
        if (!version.equals(PSD_VERSION)) {
            throw new IOException("File error: The version of this file is not correct");
        }
        stream.skipBytes(6 + 2 + 4 + 4 + 2 + 2);
    }
}
