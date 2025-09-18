package com.otulp.pluto_payments_backend;

import com.otulp.pluto_payments_backend.Models.Device;
import com.otulp.pluto_payments_backend.Repositories.DeviceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlutoPaymentsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlutoPaymentsBackendApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner fill (DeviceRepository repo) {
//        return (args) -> {
//            repo.save(new Device(true, "E8:6B:EA:D9:05:E4", "carl_esp32"));
//        };
//    }
}
