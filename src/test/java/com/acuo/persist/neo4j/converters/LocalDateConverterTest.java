package com.acuo.persist.neo4j.converters;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class LocalDateConverterTest {

    private LocalDateConverter converter  = new LocalDateConverter();
    private LocalDate date = LocalDate.of(2016, 12, 23);
    private String dateString = "2016/12/23";
    private String dateTimeString = "2016/12/23T12:00:00.000";

    @Test
    public void toGraphProperty() throws Exception {
        String converted = converter.toGraphProperty(date);
        assertThat(converted).isEqualTo(dateString);
    }

    @Test
    public void toEntityAttribute() throws Exception {
        LocalDate converted = converter.toEntityAttribute(dateString);
        assertThat(converted).isEqualTo(date);
    }
}