package com.otulp.pluto_payments_backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice {
    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn
    @OneToOne
    private User user;

    @Column(nullable = false)
    private float totalSum;

    private enum Status{
        PENDING,
        LATE,
        PAID
    }

    @Column(columnDefinition = "enum default PENDING")
    private Status status;

    @Column(nullable = false)
    private LocalDate finalDate;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate reminderDate;

}
