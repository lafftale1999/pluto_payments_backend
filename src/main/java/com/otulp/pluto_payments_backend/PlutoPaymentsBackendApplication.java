package com.otulp.pluto_payments_backend;

import com.otulp.pluto_payments_backend.Models.Address;
import com.otulp.pluto_payments_backend.Models.Card;
import com.otulp.pluto_payments_backend.Models.Country;
import com.otulp.pluto_payments_backend.Models.Device;
import com.otulp.pluto_payments_backend.Repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlutoPaymentsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlutoPaymentsBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner fill (DeviceRepository repo, CountryRepo countryRepo, AddressRepo addressRepo,
                                   CardRepo cardRepo, UserRepo userRepo, InvoiceRepo invoiceRepo,
                                   TransactionRepo transactionRepo) {
        return (args) -> {
            // repo.save(new Device(true, "E8:6B:EA:D9:05:E4", "carl_esp32"));

            Country country = new Country(null, "Sweden", "+46");
            countryRepo.save(country);

            Address address = new Address("Artellerigatan 19", "110 32", "Stockholm", country);
            addressRepo.save(address);

            Card card = new Card("23 3F AA AC", )
        };
    }
}
