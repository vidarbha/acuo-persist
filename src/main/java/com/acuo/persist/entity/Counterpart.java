package com.acuo.persist.entity;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Labels;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.util.List;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Counterpart extends Firm {

    @Labels
    private final List<String> labels = ImmutableList.of("Counterpart");
}
