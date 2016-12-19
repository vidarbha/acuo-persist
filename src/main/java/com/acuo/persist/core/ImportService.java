package com.acuo.persist.core;

import org.neo4j.ogm.annotation.Transient;

@Transient
public interface ImportService {

    void reload();

    void reload(String... fileNames);

}