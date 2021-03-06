package com.acuo.persist.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
public class Settings implements Entity<Settings> {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Double rating;


}
