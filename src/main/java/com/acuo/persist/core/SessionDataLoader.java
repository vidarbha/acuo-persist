package com.acuo.persist.core;

import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.model.QueryStatistics;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
public class SessionDataLoader implements DataLoader {

    private final Provider<Session> sessionProvider;

    @Inject
    public SessionDataLoader(Provider<Session> sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @Transactional
    @Override
    public void purgeDatabase() {
        sessionProvider.get().purgeDatabase();
    }

    @Transactional
    @Override
    public void loadData(String query) {
        if (log.isDebugEnabled()) {
            log.info("executing query {}", query);
        }
        if (StringUtils.isEmpty(query))
            return;
        final Result result = sessionProvider.get().query(query, Collections.emptyMap());
        if (log.isDebugEnabled() && result != null) {
            QueryStatistics statistics = result.queryStatistics();
            log.debug("results: " +
                            "\n\tnodes created [{}],nodes deleted [{}]," +
                            "\n\trelations created [{}], relations deleted [{}]," +
                            "\n\tindexes created [{}], indexes deleted [{}]," +
                            "\n\tconstraints created [{}], constraints deleted [{}]," +
                            "\n\t properties set [{}]",
                    statistics.getNodesCreated(), statistics.getNodesDeleted(),
                    statistics.getRelationshipsCreated(), statistics.getRelationshipsDeleted(),
                    statistics.getIndexesAdded(), statistics.getIndexesRemoved(),
                    statistics.getConstraintsAdded(), statistics.getConstraintsRemoved(),
                    statistics.getPropertiesSet()
            );
        }
    }

    @Transactional
    @Override
    public void loadData(String... queries) {
        log.info("loading {} queries ...", queries.length);
        Arrays.stream(queries).forEach(this::loadData);
        log.info("queries loaded successfully");
    }
}
