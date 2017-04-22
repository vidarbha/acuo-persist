package com.acuo.persist.entity;

import com.acuo.common.model.margin.Types;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"statementItem"})
@ToString(exclude = {"statementItem"})
public class StatementItem<T extends StatementItem> extends Entity<T> {

    @Convert(LocalDateConverter.class)
    protected LocalDate callDate;
    protected Types.MarginType marginType;
    protected String direction;
    protected String status;
    protected String currency;
    @Convert(LocalDateConverter.class)
    protected LocalDate valuationDate;
    protected Integer parentRank;
    protected Double marginAmount;
    @Convert(LocalDateTimeConverter.class)
    protected LocalDateTime notificationTime;

    @Relationship(type = "FIRST")
    private Step firstStep;

    @Relationship(type = "LAST")
    private Step lastStep;

    @Relationship(type = "MATCHED_TO_EXPECTED", direction = Relationship.INCOMING)
    private StatementItem statementItem;
}
