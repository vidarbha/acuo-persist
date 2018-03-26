package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NodeEntity
@Data
public class ServiceError implements Entity<ServiceError> {

    @Id
    @GeneratedValue
    private Long id;

    private String code;
    private String type;
    private String message;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime dateTime;

    @Convert(LocalDateConverter.class)
    private LocalDate valuationDate;
}