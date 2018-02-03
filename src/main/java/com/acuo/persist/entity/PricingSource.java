package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.PricingProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class PricingSource extends Entity<PricingSource> {

    @Index(unique = true)
    private PricingProvider name;

}
