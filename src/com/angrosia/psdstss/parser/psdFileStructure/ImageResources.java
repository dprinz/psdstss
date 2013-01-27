package com.angrosia.psdstss.parser.psdFileStructure;

import com.angrosia.psdstss.model.PsdFileContent.Slice;
import com.angrosia.psdstss.model.PsdFileContent.SlicesResource;
import com.angrosia.psdstss.parser.PsdInputStream;

import java.io.IOException;

/**
 * @link http://www.adobe.com/devnet-apps/photoshop/fileformatashtml/PhotoshopFileFormats.htm#50577409_19931
 */
public class ImageResources {
    private final String PSD_TAG = "8BIM";
    private final Integer PSD_SLICE_ID = 1050;
    private SlicesResource slicesResource;

    public void registerConsumer(SlicesResource slicesResource) {
        this.slicesResource = slicesResource;
    }

    public void parse(PsdInputStream stream) throws IOException {
        int length = stream.readInt();
        int position = stream.getPosition();
        while (length > 0) {
            String tag = stream.readAsciiString(4);
            if (!tag.equals(PSD_TAG)) {
                throw new IOException("File error: There is no Resource Section");
            }
            int resourceId = stream.readShort();
            String blockName = stream.readEvenPadString();
            int sizeResourceData = stream.readInt();

            if ((sizeResourceData & 0x01) == 1) {
                sizeResourceData++;
            }

            length -= 11 + blockName.length() + sizeResourceData;
            int storePosition = stream.getPosition();

            if (sizeResourceData > 0 && resourceId == PSD_SLICE_ID) {
                parseSlices(stream);
            }

            stream.skipBytes(sizeResourceData - (stream.getPosition() - storePosition));
        }
        stream.skipBytes(length - (stream.getPosition() - position));
    }

    /**
     * 0x041A, 1050, Slices
     */
    private void parseSlices(PsdInputStream stream) throws IOException {
        Integer version = stream.readInt();
        if (version == 6) {
            Integer top = stream.readInt();
            Integer left = stream.readInt();
            Integer bottom = stream.readInt();
            Integer right = stream.readInt();
            String groupName = stream.readAsciiString();
            if (slicesResource != null) {
                slicesResource.setVersion(version);
                slicesResource.setTop(top);
                slicesResource.setLeft(left);
                slicesResource.setBottom(bottom);
                slicesResource.setRight(right);
                slicesResource.setName(groupName);
            }
            Integer numberOfSlices = stream.readInt();
            for (int i = 0; i < numberOfSlices; i++) {
                Integer sliceId = stream.readInt();
                Integer groupId = stream.readInt();
                Integer origin = stream.readInt();
                Integer associatedLayerId = null;
                if (origin == 1) {
                     associatedLayerId = stream.readInt();
                }
                String sliceName = stream.readUnicodeString();
                Integer type = stream.readInt();
                Integer sliceLeft = stream.readInt();
                Integer sliceTop = stream.readInt();
                Integer sliceRight = stream.readInt();
                Integer sliceBottom = stream.readInt();
                String url = stream.readUnicodeString();
                String target = stream.readUnicodeString();
                String message = stream.readUnicodeString();
                String altTag = stream.readUnicodeString();
                Boolean cellIsHtml = stream.readBoolean();
                String cellText = stream.readUnicodeString();
                Integer horizontalAlignment = stream.readInt();
                Integer verticalAlignment = stream.readInt();
                Byte alphaColor = stream.readByte();
                Byte red = stream.readByte();
                Byte green = stream.readByte();
                Byte blue = stream.readByte();
                if (this.slicesResource != null) {
                    Slice slice = new Slice();
                    slice.setId(sliceId);
                    slice.setGroupId(groupId);
                    slice.setAssociatedLayerId(associatedLayerId);
                    slice.setName(sliceName);
                    slice.setType(type);
                    slice.setLeft(sliceLeft);
                    slice.setTop(sliceTop);
                    slice.setRight(sliceRight);
                    slice.setBottom(sliceBottom);
                    slice.setUrl(url);
                    slice.setTarget(target);
                    slice.setMessage(message);
                    slice.setAltTag(altTag);
                    slice.setCellIsHtml(cellIsHtml);
                    slice.setCellText(cellText);
                    slice.setHorizontalAlignment(horizontalAlignment);
                    slice.setVerticalAlignment(verticalAlignment);
                    slice.setAlpha(alphaColor);
                    slice.setRed(red);
                    slice.setGreen(green);
                    slice.setBlue(blue);
                    this.slicesResource.addSlice(slice);
                }
            }
        }
    }
}
