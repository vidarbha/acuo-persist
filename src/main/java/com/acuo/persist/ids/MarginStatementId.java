package com.acuo.persist.ids;

import com.acuo.common.type.TypedString;
import com.acuo.common.util.ArgChecker;
import org.joda.convert.FromString;

public class MarginStatementId extends TypedString<MarginStatementId>{

    private MarginStatementId(String name) {
        super(name);
    }

    @FromString
    public static MarginStatementId fromString(String id) {
        ArgChecker.notNull(id, "id");
        return new MarginStatementId(id);
    }
}
