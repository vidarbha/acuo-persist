package com.acuo.persist.learning;

import com.acuo.persist.utils.IDGen;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class IDGenTest {

    @Test
    public void testGenerateFromString() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String value = "2017/04/26-a9"+"-"+"Variation-Client";
        String encoded = IDGen.encode(value);

        assertEquals(encoded, IDGen.encode("2017/04/26-a9-Variation-Client"));
        assertNotEquals(encoded, IDGen.encode("2017/04/26-a8-Variation-Client"));
        String a = "2017/04/26-a9";
        String b = "-";
        String c = "Variation-Client";
        assertEquals(encoded, IDGen.encode(new StringBuilder().append(a).append(b).append(c).toString()));
    }

}
