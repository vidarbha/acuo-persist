package com.acuo.persist.ids;

import com.acuo.common.type.TypedString;
import com.acuo.common.util.ArgChecker;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.convert.FromString;

public class PortfolioId extends TypedString<PortfolioId> {

    public PortfolioId(String name) {
        super(name);
    }

    @FromString
    @JsonCreator
    public static PortfolioId fromString(@JsonProperty("name") String id) {
        ArgChecker.notNull(id, "id");
        return new PortfolioId(id);
    }

}
