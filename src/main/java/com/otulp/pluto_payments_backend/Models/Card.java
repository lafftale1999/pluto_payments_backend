package com.otulp.pluto_payments_backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Card {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String cardNum;

    @Column
    private String pin;

    @Column
    private LocalDate expiryDate;

    @Column
    private LocalDate issueDate;

    @Column
    private boolean isActive;

    @Column(columnDefinition = "int default 0")
    private int wrongEntries;

    public Card(String cardNum, float pin, LocalDate expiryDate, LocalDate issueDate, boolean isActive) {
        this.cardNum = cardNum;
        this.pin = pin;
        this.expiryDate = expiryDate;
        this.issueDate = issueDate;
        this.isActive = isActive;
    }

}
