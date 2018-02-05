package com.acuo.persist.utils;

import com.acuo.persist.core.DataLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.QueryStatistics;
import org.neo4j.graphdb.Result;
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
        try(Transaction tx = databaseService.beginTx()) {
            databaseService.execute("MATCH (n) OPTIONAL MATCH (n)-[r0]-() DELETE r0, n");
            tx.success();
        }
    }

    @Override
    public void loadData(String query) {
        if (log.isDebugEnabled()) {
            log.info("executing query {}", query);
        }
        if (StringUtils.isEmpty(query)) {
            return;
        }
        try(Transaction tx = databaseService.beginTx()) {
            Result result = databaseService.execute(query, Collections.emptyMap());
            if (log.isDebugEnabled() && result != null) {
                QueryStatistics statistics = result.getQueryStatistics();
                log.debug("results: " +
                        "\n\tnodes created [{}], deleted [{}]," +
                        "\n\trelations created [{}], deleted [{}]," +
                        "\n\tindexes created [{}], deleted [{}]," +
                        "\n\tconstraints created [{}], deleted [{}]," +
                        "\n\tproperties set [{}]",
                        statistics.getNodesCreated(), statistics.getNodesDeleted(),
                        statistics.getRelationshipsCreated(), statistics.getRelationshipsDeleted(),
                        statistics.getIndexesAdded(), statistics.getIndexesRemoved(),
                        statistics.getConstraintsAdded(), statistics.getConstraintsRemoved(),
                        statistics.getPropertiesSet()
                );
            }
            tx.success();
        }
    }

    @Override
    public void loadData(String... queries) {
        log.info("loading {} queries ...", queries.length);
        Arrays.stream(queries).forEach(this::loadData);
        log.info("queries loaded successfully");
    }
}