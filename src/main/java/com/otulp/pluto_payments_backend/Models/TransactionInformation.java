package com.otulp.pluto_payments_backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TransactionInformation {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private float cost;

    @Column(nullable = false)
    private LocalDate date;

    @JoinColumn
    @ManyToOne(optional = false)
    private Device device;

    @JoinColumn
    @ManyToOne(optional = false)
    private Customer user;

    @JoinColumn
    @ManyToOne(optional = false)
    private Card card;

    public TransactionInformation(float cost, LocalDate date, Device device, Customer user, Card card) {
        this.cost = cost;
        this.date = date;
        this.device = device;
        this.user = user;
        this.card = card;
    }
}
