package com.acuo.persist.utils;

import com.google.inject.persist.Transactional
import org.neo4j.ogm.session.Session

import javax.inject.Inject
import javax.inject.Provider

@Transactional
class SessionHelper {

    @Inject
    Provider<Session> sessionProvider

    def execute(query, parameters) {
        return sessionProvider.get().query(query, parameters)
    }
}