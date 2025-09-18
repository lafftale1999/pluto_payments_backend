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
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private float cost;

    @Column(nullable = false)
    private LocalDate date;

    @JoinColumn
    @OneToOne
    private Device device;

    @JoinColumn
    @OneToOne
    private User user;

    public Transaction(float cost, LocalDate date, Device device, User user) {
        this.cost = cost;
        this.date = date;
        this.device = device;
        this.user = user;
    }

}
