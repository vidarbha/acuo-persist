package com.acuo.persist.ids;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class PortfolioIdTest {

    @Test
    public void fromString() throws Exception {
        ObjectMapper om = new ObjectMapper();

        String testAString = om.writeValueAsString(PortfolioId.fromString("p2")); // error here!

        PortfolioId newTestA = om.readValue(testAString, PortfolioId.class);
    }

}