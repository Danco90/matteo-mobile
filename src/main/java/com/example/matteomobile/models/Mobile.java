package com.example.matteomobile.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Mobile
{
    @Id
    @GeneratedValue()
    private Long id;
    private String brand;
    private String model;
    private boolean isAvailable;
    /*private LocalDateTime bookingDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;*/
    private String technology;
    private String _2gBands;
    private String _3gBands;
    private String _4gBands;

}
