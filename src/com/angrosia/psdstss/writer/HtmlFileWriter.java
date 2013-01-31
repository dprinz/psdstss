package com.angrosia.psdstss.writer;

import com.angrosia.psdstss.model.PsdFileContent.Slice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlFileWriter extends FileWriter {
    private List<String> styleSheetFiles = new ArrayList<String>();
    private List<String> tableRows = new ArrayList<String>();


    public HtmlFileWriter(File htmlFile) throws IOException {
        super(htmlFile.getCanonicalPath());
    }

    private void addStylesheetFileUnique(String styleSheetFile) {
        if (!styleSheetFiles.contains(styleSheetFile)) {
            styleSheetFiles.add(styleSheetFile);
        }
    }

    private void addTableRow(String name, Slice slice) {
        tableRows.add("<div class=\"x y\" style=\"float:left\">" + name +
                "<hr /><table>" +
                (!slice.getName().isEmpty() ? "<tr><th>Name</th><td>" + slice.getName() + "</td></tr>\r\n" : "") +
                (!slice.getUrl().isEmpty() ? "<tr><th>URL</th><td>" + slice.getUrl() + "</td></tr>\n" : "") +
                (!slice.getTarget().isEmpty() ? "<tr><th>Target</th><td>" + slice.getTarget() + "</td></tr>\n" : "") +
                (!slice.getMessage().isEmpty() ? "<tr><th>Message</th><td>" + slice.getMessage() + "</td></tr>\n" : "") +
                (!slice.getAltTag().isEmpty() ? "<tr><th>Alt-Tag</th><td>" + slice.getAltTag() + "</td></tr>\n" : "") +
                "<tr><th>Position</th><td>" + slice.getLeft() + ", " + slice.getTop() + "</td></tr>\n" +
                "<tr><th>Size</th><td>" + (slice.getRight() - slice.getLeft()) + " x " + (slice.getBottom() - slice.getTop()) + "</td></tr>\n" +
                "</table>" +
                "</div>" +
                "<div class=\"x\" style=\"float:left\"><div class=\"" + name + "\"></div></div>" +
                "<div style=\"clear:both\"></div>");
    }

    public void addSlice(String outputFile, String className, Slice slice) {
        addStylesheetFileUnique(outputFile);
        addTableRow(className, slice);
    }

    public void done() throws IOException {
        String linkFiles = "";
        String rows = "";
        for(String styleSheetFile : styleSheetFiles) {
            linkFiles = linkFiles.concat("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + styleSheetFile + "\">\r\n");
        }
        for(String tableRow : tableRows) {
            rows = rows.concat(tableRow + "\r\n");
        }
        this.write("<!DOCTYPE html>\r\n<html>\r\n<head>\r\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />" +
                "<title>Sprite Overview</title>\r\n" +
                "<style type=\"text/css\">\r\n" +
                ".x{font-family:sans-serif;background-color:yellow;margin:2px;padding:0;border:1px solid silver;}\r\n" +
                ".y{width:200px;overflow-x:auto;font-family:sans-serif;padding:5px;}\r\n" +
                "th,td{font-family:sans-serif;font-size:x-small;text-align:left;}\r\n" +
                "hr{height:1px;background-color:silver;color:silver;border:none;margin:3px 0;}\r\n" +
                "</style>\r\n" +
                linkFiles +
                "</head>\r\n<body>\r\n" +
                rows +
                "</body>\r\n</html>\r\n");

        this.flush();
        this.close();
    }
}
