package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDateTime;

@NodeEntity(label = "Error")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceError extends Entity<ServiceError> {

    private String code;
    private String type;
    private String message;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime dateTime;

}
