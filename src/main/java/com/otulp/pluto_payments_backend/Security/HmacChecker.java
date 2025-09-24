package com.otulp.pluto_payments_backend.Security;

import com.google.common.hash.Hashing;
import com.otulp.pluto_payments_backend.Models.Device;
import com.otulp.pluto_payments_backend.DTOs.PaymentDTO;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;

public class HmacChecker {
    public static boolean checkHmac(Device device, HttpHeaders headers, String rawBody) {
        // 1. Create and compare hmac
        String hmac = headers.getFirst("Authorization");
        String url = "https://" + headers.getFirst("Host") + ":443/device/authorize"; // <-------------------- CHANGE HERE WHEN GOING FOR HTTPS
        String contentType = "Content-Type: " + headers.getFirst("Content-Type");
        String bodyHex = Hashing.sha256()
                .hashString(rawBody, StandardCharsets.UTF_8)
                .toString();

        if (hmac == null) {
            return false;
        }

        String canonicalString = "POST\n" +
                url + "\n" +
                contentType + "\n" +
                bodyHex;

        System.out.println(canonicalString);

        canonicalString += device.getDeviceKey();

        String serverHmac = PlutoHasher.hashedString(canonicalString);

        System.out.println(hmac);
        System.out.println(serverHmac);

        return hmac.equals(serverHmac);
    }
}
