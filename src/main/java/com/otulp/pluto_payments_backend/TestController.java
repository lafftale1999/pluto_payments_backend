package com.otulp.pluto_payments_backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/authorize")
    public ResponseEntity<String> test_auth(@RequestBody String body) {
        System.out.println(body);

        return ResponseEntity.ok("Payment Accepted");
    }
}
