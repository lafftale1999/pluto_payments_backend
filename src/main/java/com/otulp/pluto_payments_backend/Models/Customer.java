package com.otulp.pluto_payments_backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private float balance;

    @Column(nullable = false)
    private float maxLimit;

    @Column(nullable = false)
    private float points;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public enum Type{
        NEW,
        REGULAR,
        LOYAL,
        FROZEN
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @JoinColumn
    @OneToOne(cascade = CascadeType.ALL)
    private Card card;

    @JoinColumn
    @ManyToOne(optional = false)
    private Address address;

    public Customer(String firstName, String lastName, float balance, float maxLimit, Float points, String phoneNumber, LocalDate dateOfBirth, String email, String password, Type type, Card card, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.maxLimit = maxLimit;
        this.points = points;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.type = type;
        this.card = card;
        this.address = address;
    }
}
