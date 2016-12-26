package com.acuo.persist.neo4j.converters;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateTimeConverterTest {

    private LocalDateTimeConverter converter  = new LocalDateTimeConverter();

    private LocalDateTime dateTime = LocalDateTime.of(2016, 12, 23, 19, 0, 0);
    private String dateString = "23/12/16 19:00:00";

    @Test
    public void toGraphProperty() throws Exception {
        String converted = converter.toGraphProperty(dateTime);
        assertThat(converted).isEqualTo(dateString);
    }


    @Test
    public void toEntityAttribute() throws Exception {
        LocalDateTime converted = converter.toEntityAttribute(dateString);
        assertThat(converted).isEqualTo(dateTime);
    }

}