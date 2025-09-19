package com.otulp.pluto_payments_backend;

import com.otulp.pluto_payments_backend.Models.*;
import com.otulp.pluto_payments_backend.Repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class PlutoPaymentsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlutoPaymentsBackendApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner fill (DeviceRepository repo, CountryRepo countryRepo, AddressRepo addressRepo,
//                                   CardRepo cardRepo, UserRepo userRepo, InvoiceRepo invoiceRepo,
//                                   TransactionRepo transactionRepo) {
//        return (args) -> {
//            repo.save(new Device(true, "E8:6B:EA:D9:05:E4", "carl_esp32"));
//            Device device = repo.findByMacAddress("E8:6B:EA:D9:05:E4");
//
//            Country country = new Country(null, "Sweden", "+46");
//            countryRepo.save(country);
//
//            Address address = new Address("Artellerigatan 19", "110 32", "Stockholm", country);
//            addressRepo.save(address);
//
//            LocalDate issueDate = LocalDate.of(2023, 4, 12);
//            LocalDate expiryDate = issueDate.plusYears(3);
//            Card card = new Card (
//                    "23 3F AA AC",
//                    "770c3cbf77615a1d94f88d0f9ed148823de46e4518a3f290fec2859e85b501ef",
//                    expiryDate, issueDate, true, 0
//            );
//
//            Customer user = new Customer(
//                    "Rigbert", "Moggby", 9300f, 20000f, 4560f, "0707837291",
//                    LocalDate.of(1990, 1, 1),
//                    "moggingOnYou@gmail.com", "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9",
//                    Customer.Type.NEW, card, address
//            );
//
//            userRepo.save(user);
//            transactionRepo.save(new TransactionInformation(null, 3240f, LocalDate.now(), device, user, card));
//            transactionRepo.save(new TransactionInformation(null, 4930f, LocalDate.now(), device, user, card));
//            transactionRepo.save(new TransactionInformation(null, 1130f, LocalDate.now(), device, user, card));
//
//            LocalDate invoiceIssueDate = LocalDate.now();
//            LocalDate invoiceExpiryDate = invoiceIssueDate.plusMonths(1);
//            LocalDate invoiceReminderDate = invoiceExpiryDate.plusMonths(1);
//
//            invoiceRepo.save(new Invoice(null, user, 9300f, Invoice.Status.PENDING, invoiceIssueDate, invoiceExpiryDate, invoiceReminderDate));
//        };
//    }
}
