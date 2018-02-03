package com.acuo.persist.core;

import com.acuo.persist.utils.GraphData;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CypherFileSpliter {

    private static final String ENCODING = "UTF-8";
    private final String workingDirectory;
    private final String delimiter;
    private final String directoryTemplate;

    private CypherFileSpliter(String workingDirectory, String directoryTemplate, String delimiter) {
        this.workingDirectory = workingDirectory;
        this.directoryTemplate = directoryTemplate;
        this.delimiter = delimiter;
    }

    public static CypherFileSpliter of(String workingDirectory) {
        return CypherFileSpliter.newBuilder(workingDirectory).build();
    }

    public static CypherFileSpliterBuilder newBuilder(String workingDirectory) {
        return new CypherFileSpliterBuilder(workingDirectory);
    }

    public List<String> splitByLine(String fileName) {
        try (BufferedReader br = Files
                .newBufferedReader(Paths.get(String.format(directoryTemplate, workingDirectory, fileName)))) {
            return br.lines().filter(x -> !x.trim().isEmpty()).collect(Collectors.toList());
        } catch (IOException e) {
            return ImmutableList.of();
        }
    }

    public List<String> splitByDefaultDelimiter(String fileName) {
        return splitByDelimiter(fileName, delimiter);
    }

    public List<String> splitByDelimiter(String fileName, String delim) {
        try {
            //String filePath = String.format(directoryTemplate, workingDirectory, fileName);
            String content = GraphData.readFile(fileName);
            return Stream.of(content).map(w -> w.split(delim)).flatMap(Arrays::stream).filter(x -> !x.trim().isEmpty())
                    .map(x -> chomp(x)).collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            log.warn(e.getMessage());
            return ImmutableList.of();
        }
    }

    private static String chomp(String value) {
        return StringUtils.chomp(value).replaceAll("\r", "").replaceAll("\n", "");
    }

    public static class CypherFileSpliterBuilder {

        private final String workingDirectory;
        private String directoryTemplate = "%s/cypher/%s";
        private String delimiter = ";";

        private CypherFileSpliterBuilder(String workingDirectory) {
            this.workingDirectory = workingDirectory;
        }

        public CypherFileSpliterBuilder withDirectoryTemplate(String directoryTemplate) {
            this.directoryTemplate = directoryTemplate;
            return this;
        }

        public CypherFileSpliterBuilder withDelimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public CypherFileSpliter build() {
            return new CypherFileSpliter(workingDirectory, directoryTemplate, delimiter);
        }
    }

}
