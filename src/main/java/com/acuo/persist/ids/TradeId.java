package com.acuo.persist.ids;

import com.acuo.common.type.TypedString;
import com.acuo.common.util.ArgChecker;
import org.joda.convert.FromString;

public class TradeId extends TypedString<TradeId> {

    private TradeId(String name) {
        super(name);
    }

    @FromString
    public static TradeId fromString(String id) {
        ArgChecker.notNull(id, "id");
        return new TradeId(id);
    }

}
