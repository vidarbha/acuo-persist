package com.acuo.persist.utils;

import com.acuo.persist.core.DataLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.harness.ServerControls;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Singleton
public class DirectDataLoader implements DataLoader {

    private final GraphDatabaseService databaseService;

    @Inject
    public DirectDataLoader(ServerControls serverControls) {
        this.databaseService = serverControls.graph();
    }

    @Override
    public void purgeDatabase() {
        final Transaction transaction = databaseService.beginTx();
        databaseService.execute("MATCH (n) OPTIONAL MATCH (n)-[r0]-() DELETE r0, n");
        transaction.close();
    }

    @Override
    public void loadData(String query) {
        if (StringUtils.isEmpty(query))
            return;
        try(Transaction tx = databaseService.beginTx()) {
            databaseService.execute(query, Collections.emptyMap());
            tx.success();
        }
    }

    @Override
    public void loadData(String... queries) {
        Arrays.stream(queries).forEach(query -> {
            loadData(query);
        });
    }
}