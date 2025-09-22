package com.otulp.pluto_payments_backend.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Device {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private boolean isApproved;

    @Column(nullable = false, unique = true)
    private String macAddress;

    @Column(nullable = false, unique = true)
    private String deviceKey;

    @Column(nullable = false)
    private String companyName;

    public Device(boolean isApproved, String macAddress, String deviceKey, String companyName) {
        this.isApproved = isApproved;
        this.macAddress = macAddress;
        this.deviceKey = deviceKey;
        this.companyName = companyName;
    }
}
