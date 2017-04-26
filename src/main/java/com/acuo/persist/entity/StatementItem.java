package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.Side;
import com.acuo.persist.entity.enums.StatementDirection;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static com.acuo.common.model.margin.Types.MarginType;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"statementItem", "marginStatement", "children"})
@ToString(exclude = {"statementItem", "marginStatement", "children"})
public class StatementItem<T extends StatementItem> extends Entity<T> {

    @Convert(LocalDateConverter.class)
    protected LocalDate callDate;
    protected MarginType marginType;
    protected Double marginAmount;
    protected StatementDirection direction;
    protected StatementStatus status;
    @Convert(CurrencyConverter.class)
    protected Currency currency;
    @Convert(LocalDateConverter.class)
    protected LocalDate valuationDate;
    @Convert(LocalDateTimeConverter.class)
    protected LocalDateTime notificationTime;
    protected Integer parentRank;
    protected Side side;

    @Relationship(type = "PART_OF")
    private MarginStatement marginStatement;

    @Relationship(type = "FIRST")
    protected Step firstStep;

    @Relationship(type = "LAST")
    protected Step lastStep;

    @Relationship(type = "MATCHED_TO_EXPECTED", direction = Relationship.INCOMING)
    private StatementItem statementItem;

    @Relationship(type = "CHILD_OF", direction = Relationship.INCOMING)
    private Set<ChildOf> children;
}
