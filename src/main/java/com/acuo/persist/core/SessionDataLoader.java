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
        if (StringUtils.isEmpty(query))
            return;
        final Result result = sessionProvider.get().query(query, Collections.emptyMap());
        final QueryStatistics queryStatistics = result.queryStatistics();
        log.info("results: \n\tnodes created [{}],\n\t properties set [{}], \n\trelationships created [{}]",
                queryStatistics.getNodesCreated(),
                queryStatistics.getPropertiesSet(),
                queryStatistics.getRelationshipsCreated());
    }

    @Transactional
    @Override
    public void loadData(String... queries) {
        log.info("loading {} queries ...", queries.length);
        final Session session = sessionProvider.get();
        Arrays.stream(queries).forEach(query -> {
            if (log.isDebugEnabled()) {
                log.debug("loading query {} ", query);
            }
            session.query(query, Collections.emptyMap());
        });
        log.info("queries loaded successfully", queries.length);
    }
}
