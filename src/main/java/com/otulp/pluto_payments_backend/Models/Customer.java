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
    private float creditUsed;

    @Column(nullable = false)
    private float creditLimit;

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

    public Customer(String firstName, String lastName, float creditUsed, float creditLimit, Float points, String phoneNumber, LocalDate dateOfBirth, String email, String password, Type type, Card card, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.creditUsed = creditUsed;
        this.creditLimit = creditLimit;
        this.points = points;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.type = type;
        this.card = card;
        this.address = address;
    }

    public float getAvailableCredit() {
        return creditLimit - creditUsed;
    }

    public void updateCredit(float amount) {
        creditUsed += amount;

        float pointRegulator = switch (this.type) {
            case NEW -> 0.8f;
            case REGULAR -> 1;
            case LOYAL -> 1.2f;
            case FROZEN -> 0;
        };

        points += amount * pointRegulator;
    }
}
