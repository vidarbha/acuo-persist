package com.acuo.persist.utils;

import com.acuo.common.util.ArgChecker;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.CRC32;

public class IDGen {

    private static Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private static Base64.Decoder decoder = Base64.getUrlDecoder();
    private static Charset UTF8 = StandardCharsets.UTF_8;

    private IDGen() {}

    public static String encode(String value) {
        ArgChecker.notNull(value, "value");
        String result = UUID.nameUUIDFromBytes(value.getBytes(UTF8)).toString();
        return Long.toHexString(convertNumber(result));
    }

    public static long convertNumber(String uuid){
        CRC32 crc32 = new CRC32();
        crc32.update(uuid.getBytes());
        return crc32.getValue();
    }
}
