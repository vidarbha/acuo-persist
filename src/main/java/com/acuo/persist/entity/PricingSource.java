package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.PricingProvider;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
public class PricingSource implements Entity<PricingSource> {

    @Id
    @GeneratedValue
    private Long id;

    @Id
    private PricingProvider name;

}
