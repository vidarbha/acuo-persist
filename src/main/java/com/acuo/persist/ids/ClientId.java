package com.acuo.persist.ids;

import com.acuo.common.type.TypedString;
import com.acuo.common.util.ArgChecker;
import org.joda.convert.FromString;

public class ClientId extends TypedString<ClientId> {

    private ClientId(String name) {
        super(name);
    }

    @FromString
    public static ClientId fromString(String id) {
        ArgChecker.notNull(id, "id");
        return new ClientId(id);
    }
}
