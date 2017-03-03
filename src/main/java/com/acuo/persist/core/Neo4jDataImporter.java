package com.acuo.persist.core;

import com.acuo.common.util.ArgChecker;
import com.acuo.persist.configuration.PropertiesHelper;
import com.acuo.persist.utils.GraphData;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    private final String dataBranch;
    private final String dataDirPath;
    private final String directoryTemplate;
    private Map<String, String> substitutions = new HashMap<>();

    @Inject
    public Neo4jDataImporter(DataLoader loader,
                             @Named(PropertiesHelper.ACUO_DATA_DIR) String dataDir,
                             @Named(PropertiesHelper.ACUO_DATA_BRANCH) String dataBranch,
                             @Named(PropertiesHelper.ACUO_DATA_IMPORT_LINK) String dataImportLink,
                             @Named(PropertiesHelper.ACUO_CYPHER_DIR_TEMPLATE) String directoryTemplate) {
        this.loader = loader;
        this.workingDirPath = dataDir;
        this.dataBranch = dataBranch;
        this.dataDirPath = GraphData.getDataLink(dataImportLink);
        this.directoryTemplate = directoryTemplate;
        substitutions.put("%workingDirPath%", workingDirPath);
        substitutions.put("%dataImportLink%", dataDirPath);
        LOG.info("data importer created with working [{}] branch [{}] data [{}] template [{}]", workingDirPath,
                dataBranch,
                dataDirPath,
                directoryTemplate);
    }

    @Override
    public void importFiles(String branch, String... fileNames) {
        if (branch == null)
            branch = dataBranch;
        final String value = branch;
        Arrays.asList(fileNames).stream().forEach(f -> importFile(value, f));
    }

    private void importFile(String branch, String fileName) {
        try {
            substitutions.put("%branch%", branch);
            String filePath = String.format(directoryTemplate, workingDirPath, fileName);
            filePath = buildQuery(filePath, substitutions);
            LOG.info("Importing files [{}] from {}", fileName, filePath);
            String file = GraphData.readFile(filePath);
            String query = buildQuery(file, substitutions);
            loader.loadData(query);
        } catch (Exception e) {
            LOG.error("an error occured while importing file {}", fileName, e);
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