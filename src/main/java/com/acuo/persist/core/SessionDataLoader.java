package com.acuo.persist.core;

import com.acuo.persist.configuration.PropertiesHelper;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.List;

public class SessionDataLoader implements DataLoader {

    public static final String LOAD_ALL_CQL = "data.cql";
    public static final String DELETE_ALL_CQL = "deleteAll.cql";
    public static final String CONSTRAINTS_CQL = "constraints.cql";

    private final Provider<Session> sessionProvider;
    private final CypherFileSpliter spliter;

    @Inject
    public SessionDataLoader(Provider<Session> sessionProvider, @Named(PropertiesHelper.ACUO_DATA_DIR) String dataDirectory) {
        this.sessionProvider = sessionProvider;
        this.spliter = CypherFileSpliter.of(dataDirectory);
    }

    @Transactional
    @Override
    public void purgeDatabase() {
        sessionProvider.get().purgeDatabase();
    }

    @Override
    public void loadAll() {
        loadDataFile(LOAD_ALL_CQL);
    }

    @Override
    public void createConstraints() {
        loadDataFile(CONSTRAINTS_CQL);
    }

    @Override
    public void loadDataFile(String fileName) {
        List<String> lines = spliter.splitByDefaultDelimiter(fileName);
        for (String query : lines) {
            loadData(query);
        }
    }

    @Transactional
    @Override
    public void loadData(String query) {
        if (StringUtils.isEmpty(query))
            return;
        sessionProvider.get().query(query, Collections.emptyMap());
    }
}
