package com.acuo.persist.entity;

import com.acuo.persist.ids.PortfolioId;
import com.acuo.persist.neo4j.converters.TypedStringConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.util.Set;

import static com.acuo.persist.neo4j.converters.TypedStringConverter.*;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"valuations"})
@ToString(exclude = {"valuations"})
public class Portfolio extends Entity<Portfolio> {

    @Property(name="id")
    @Index(primary = true)
    @Convert(PortfolioIdConverter.class)
    private PortfolioId portfolioId;

    private String name;

    private String currency;

    @Relationship(type = "IS_COMPOSED_OF")
    private Set<Exposure> exposures;

    @Relationship(type = "IS_COMPOSED_OF")
    private Set<Asset> assets;

    @Relationship(type="FOLLOWS")
    private Agreement agreement;

}
