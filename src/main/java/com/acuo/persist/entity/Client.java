package com.acuo.persist.entity;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Labels;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Client extends Firm {

    @Labels
    private final List<String> labels = ImmutableList.of("Client");

    @Relationship(type = "HAS")
    private Set<Settings> settings;

}
