package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDateTime;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class StepError extends Step {

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime dateTime;

    private String statusCode;
    private int errorCode;
    private String errorMsg;
    private String errorDesc;
    private String statusDesc;
    private StatementStatus statusAimed;
}