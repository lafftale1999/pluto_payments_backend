package com.otulp.pluto_payments_backend.Security;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class PlutoHasher {
    public static String hashedString (String word) {
        return Hashing.sha256()
                .hashString(word, StandardCharsets.UTF_8)
                .toString();
    }
}
