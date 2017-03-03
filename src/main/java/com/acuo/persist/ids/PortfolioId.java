package com.acuo.persist.ids;

import com.acuo.common.type.TypedString;
import com.acuo.common.util.ArgChecker;
import org.joda.convert.FromString;

public class PortfolioId extends TypedString<PortfolioId> {

    private PortfolioId(String name) {
        super(name);
    }

    @FromString
    public static PortfolioId fromString(String id) {
        ArgChecker.notNull(id, "id");
        return new PortfolioId(id);
    }

}
