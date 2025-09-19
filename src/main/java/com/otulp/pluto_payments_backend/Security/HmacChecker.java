package com.otulp.pluto_payments_backend.Security;

import com.google.common.hash.Hashing;
import com.otulp.pluto_payments_backend.Models.Device;
import com.otulp.pluto_payments_backend.DTOs.PaymentDTO;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;

public class HmacChecker {
    public static boolean checkHmac(PaymentDTO payment, Device device, HttpHeaders headers, String rawBody) {
        // 1. Create and compare hmac
        String hmac = headers.getFirst("Authorization");
        String url = "http://" + headers.getFirst("Host") + "/authorize"; // <-------------------- CHANGE HERE WHEN GOING FOR HTTPS
        String content = "Content-Type: " + headers.getFirst("Content-Type");
        String bodyHex = Hashing.sha256()
                .hashString(rawBody, StandardCharsets.UTF_8)
                .toString();

        if (hmac == null) {
            return false;
        }

        String canonicalString = "POST\n" +
                url + "\n" +
                content + "\n" +
                bodyHex;

        canonicalString += device.getDeviceKey();

        String serverHmac = Hashing.sha256()
                .hashString(canonicalString, StandardCharsets.UTF_8)
                .toString();

        return hmac.equals(serverHmac);
    }
}
