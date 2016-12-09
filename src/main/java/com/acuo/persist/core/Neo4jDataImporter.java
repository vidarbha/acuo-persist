package com.acuo.persist.core;

import com.acuo.common.util.ArgChecker;
import com.acuo.persist.configuration.PropertiesHelper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Singleton
public class Neo4jDataImporter implements DataImporter {

    private static final Logger LOG = LoggerFactory.getLogger(Neo4jDataImporter.class);

    private final DataLoader loader;
    private static final String ENCODING = "UTF-8";
    private final String workingDirPath;
    private final String dataDirPath;
    private final String directoryTemplate;
    private Map<String, String> substitutions = new HashMap<>();

    @Inject
    public Neo4jDataImporter(DataLoader loader, @Named(PropertiesHelper.ACUO_DATA_DIR) String dataDir,
                             @Named(PropertiesHelper.ACUO_DATA_IMPORT_LINK) String dataImportLink,
                             @Named(PropertiesHelper.ACUO_CYPHER_DIR_TEMPLATE) String directoryTemplate) {
        this.loader = loader;
        this.workingDirPath = dataDir;
        this.dataDirPath = getDataLink(dataImportLink);
        this.directoryTemplate = directoryTemplate;
        substitutions.put("%workingDirPath%", workingDirPath);
        substitutions.put("%dataImportLink%", dataDirPath);
        LOG.info("data importer created with working [{}] data [{}] template [{}]", workingDirPath, dataDirPath,
                directoryTemplate);
    }

    private String getDataLink(String dataImportLink) {
        if (dataImportLink.startsWith("file://") || dataImportLink.startsWith("http://"))
            return dataImportLink;
        return "file://" + Resources.getResource(dataImportLink).getPath();
    }

    @Override
    public void importFiles(String... fileNames) {
        Arrays.asList(fileNames).stream().forEach(f -> importFile(f));
    }

    private void importFile(String fileNames) {
        try {
            String filePath = String.format(directoryTemplate, workingDirPath, fileNames);
            LOG.info("Importing files [{}] from {}", fileNames, filePath);
            String file = Resources.toString(Resources.getResource(filePath), Charsets.UTF_8); // FileUtils.getFile(filePath);
            String query = buildQuery(file, substitutions);
            loader.loadData(query);
        } catch (Exception e) {
            LOG.error("an error occured while importing file {}", fileNames, e);
        }
    }

    private String buildQuery(File cypherFile, Map<String, String> substitutions) throws IOException {
        ArgChecker.notNull(cypherFile, "cypherFile");
        ArgChecker.isTrue(cypherFile.exists(), "File: " + cypherFile.getName() + " does not exist!");
        String query = FileUtils.readFileToString(cypherFile, ENCODING);
        query = replacePlaceHolders(query, substitutions.entrySet());
        LOG.debug("{}", query);
        return query;
    }

    private String buildQuery(String file, Map<String, String> substitutions) throws IOException {
        ArgChecker.notNull(file, "file");
        String query = replacePlaceHolders(file, substitutions.entrySet());
        LOG.debug("{}", query);
        return query;
    }

    private String replacePlaceHolders(final String query, Set<Map.Entry<String, String>> placeHolders) {
        return placeHolders.stream()
                .map(entry -> (Function<String, String>) data -> data.replaceAll(entry.getKey(), entry.getValue()))
                .reduce(Function.identity(), Function::andThen).apply(query);
    }
}
