package com.acuo.persist.neo4j.converters;

import com.acuo.common.ids.PortfolioName;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TypedStringConverterTest {

    private final TypedStringConverter.PortfolioNameConverter converter = new TypedStringConverter.PortfolioNameConverter();
    private final PortfolioName pName = PortfolioName.fromString("p2");

    @Test
    public void toGraphProperty() {
        String p2 = converter.toGraphProperty(pName);
        assertThat(p2).isNotNull().isEqualTo("p2");
    }

    @Test
    public void toEntityAttribute() {
        PortfolioName p2 = converter.toEntityAttribute("p2");
        assertThat(p2).isNotNull().isEqualTo(pName);
    }

}