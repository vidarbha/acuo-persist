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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class DirectDataLoader implements DataLoader {

    private final GraphDatabaseService databaseService;

    private static final boolean allInOne = false;

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
        long stat = System.nanoTime();
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
        long end = System.nanoTime();
        if (log.isDebugEnabled()) {
            log.debug("query {} took {}", query, TimeUnit.NANOSECONDS.toMillis(end-stat));
        }
    }

    @Override
    public void loadData(String... queries) {
        log.info("loading {} queries ...", queries.length);
        if (allInOne) {
            final String bigQuery = Arrays.stream(queries)
                    .collect(Collectors.joining(" WITH count(*) as dummy " + System.getProperty("line.separator")));
            loadData(bigQuery.replaceAll(";", ""));
        } else {
            Arrays.stream(queries).forEach(this::loadData);
        }
        log.info("queries loaded successfully");
    }
}