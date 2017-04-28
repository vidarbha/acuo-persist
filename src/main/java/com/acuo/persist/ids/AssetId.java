package com.acuo.persist.ids;

import com.acuo.common.type.TypedString;
import com.acuo.common.util.ArgChecker;
import org.joda.convert.FromString;

public class AssetId extends TypedString<AssetId> {

    private AssetId(String name) {
        super(name);
    }

    @FromString
    public static AssetId fromString(String id) {
        ArgChecker.notNull(id, "id");
        return new AssetId(id);
    }

}
