package com.acuo.persist.entity;

import com.acuo.common.ids.PortfolioId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.acuo.persist.neo4j.converters.TypedStringConverter.PortfolioIdConverter;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = {"id","errors"})
@ToString(exclude = {"errors"})
public class Portfolio implements Entity<Portfolio> {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "id")
    @Convert(PortfolioIdConverter.class)
    private PortfolioId portfolioId;

    private String name;

    private String currency;

    @Relationship(type = "IS_COMPOSED_OF")
    private Set<Exposure> exposures;

    @Relationship(type = "IS_COMPOSED_OF")
    private Set<Asset> assets;

    @Relationship(type = "FOLLOWS")
    private Agreement agreement;

    @Relationship(type = "ENCOUNTERS")
    private List<ServiceError> errors = new ArrayList<>();

    public void addErrors(ServiceError error) {
        errors.add(error);
    }

    public void addAllErrors(List<ServiceError> error) {
        errors.addAll(errors);
    }

}
