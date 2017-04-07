package com.acuo.persist.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;

@Slf4j
public class GraphData {

    private GraphData(){}

    public static String getDataLink(String dataImportLink) {
        if (dataImportLink.startsWith("file://") || dataImportLink.startsWith("http://") || dataImportLink.startsWith("https://"))
            return dataImportLink;
        return "file://" + Resources.getResource(dataImportLink).getPath();
    }

    public static String readFile(String filePath) throws IOException, URISyntaxException {
        String path = GraphData.getDataLink(filePath);
        return IOUtils.toString(new URI(path), Charsets.UTF_8);
    }

    public static DateTimeFormatter getStatementDateFormatter() {
        return DateTimeFormatter.ofPattern("yyyy/MM/dd");
    }
}