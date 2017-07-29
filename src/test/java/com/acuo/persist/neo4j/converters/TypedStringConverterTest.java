package com.acuo.persist.neo4j.converters;

import com.acuo.common.model.ids.PortfolioId;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TypedStringConverterTest {

    private final TypedStringConverter.PortfolioIdConverter converter = new TypedStringConverter.PortfolioIdConverter();
    private final PortfolioId pId = PortfolioId.fromString("p2");

    @Test
    public void toGraphProperty() throws Exception {
        String p2 = converter.toGraphProperty(pId);
        assertThat(p2).isNotNull().isEqualTo("p2");
    }

    @Test
    public void toEntityAttribute() throws Exception {
        PortfolioId p2 = converter.toEntityAttribute("p2");
        assertThat(p2).isNotNull().isEqualTo(pId);
    }

}