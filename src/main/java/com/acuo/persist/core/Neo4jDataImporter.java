package com.acuo.persist.core;

import com.acuo.common.cache.manager.CacheManager;
import com.acuo.common.cache.manager.Cacheable;
import com.acuo.common.cache.manager.CachedObject;
import com.acuo.persist.configuration.PropertiesHelper;
import com.acuo.persist.utils.GraphData;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.acuo.common.util.ArgChecker.notNull;

@Singleton
public class Neo4jDataImporter implements DataImporter {

    private static final Logger LOG = LoggerFactory.getLogger(Neo4jDataImporter.class);
    private static CacheManager cacheManager = new CacheManager();

    private final DataLoader loader;
    private final String workingDirPath;
    private final String dataBranch;
    private final String directoryTemplate;
    private final String dataImportFiles;
    private Map<String, String> substitutions = new LinkedHashMap<>();

    @Inject
    public Neo4jDataImporter(DataLoader loader,
                             @Named(PropertiesHelper.ACUO_DATA_DIR) String dataDir,
                             @Named(PropertiesHelper.ACUO_DATA_BRANCH) String dataBranch,
                             @Named(PropertiesHelper.ACUO_DATA_IMPORT_LINK) String dataImportLink,
                             @Named(PropertiesHelper.ACUO_DATA_IMPORT_FILES) String dataImportFiles,
                             @Named(PropertiesHelper.ACUO_CYPHER_DIR_TEMPLATE) String directoryTemplate) {
        this.loader = loader;
        this.workingDirPath = dataDir;
        this.dataBranch = dataBranch;
        String dataDirPath = GraphData.getDataLink(dataImportLink);
        this.directoryTemplate = directoryTemplate;
        this.dataImportFiles = dataImportFiles;
        substitutions.put("%workingDirPath%", workingDirPath);
        substitutions.put("%dataImportLink%", dataDirPath);
        LOG.info("data importer created with working [{}] branch [{}] data [{}] template [{}]", workingDirPath,
                dataBranch,
                dataDirPath,
                directoryTemplate);
    }

    @Override
    public void importFiles(String branch, String client, String... fileNames) {
        if (branch == null)
            branch = dataBranch;
        final String value = branch;
        Arrays.stream(fileNames).forEach(f -> importFile(value, client, f));
    }

    @Override
    public String[] filesToImport() {
        return Pattern.compile(",")
                .splitAsStream(dataImportFiles)
                .filter(Objects::nonNull)
                .map(String::trim)
                .toArray(String[]::new);
    }

    private void importFile(String branch, String client, String fileName) {
        try {
            substitutions.put("%branch%", branch);
            substitutions.put("%client%", client);
            String filePath = String.format(directoryTemplate, workingDirPath, fileName);
            filePath = buildQuery(filePath, substitutions);
            LOG.info("Importing client [{}] files [{}] from {}", client, fileName, filePath);
            String query = file(client, filePath);
            loader.loadData(query);
        } catch (Exception e) {
            LOG.error("an error occured while importing client [{}] file {}", client, fileName, e);
        }
    }

    private String file(String client, String filePath) throws IOException, URISyntaxException {
        Cacheable value = cacheManager.getCache(client + filePath);
        if (value == null) {
            String file = GraphData.readFile(filePath);
            file = buildQuery(file, substitutions);
            value = new CachedObject(file, client + filePath, 3);
            cacheManager.putCache(value);
        }
        return (String)value.getObject();
    }

    private String buildQuery(String file, Map<String, String> substitutions) {
        String query = replacePlaceHolders(notNull(file, "file"), substitutions.entrySet());
        LOG.debug("{}", query);
        return query;
    }

    private String replacePlaceHolders(final String query, Set<Map.Entry<String, String>> placeHolders) {
        return placeHolders.stream()
                .map(entry -> (Function<String, String>) data -> data.replaceAll(entry.getKey(), entry.getValue()))
                .reduce(Function.identity(), Function::andThen).apply(query);
    }
}