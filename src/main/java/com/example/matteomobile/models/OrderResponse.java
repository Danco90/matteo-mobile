package com.example.matteomobile.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse
{
    private String deviceName;
    private String brand;
    private String model;
    private boolean available;
    private String technology;
    private LocalDateTime bookingDate;
    private Customer customer;
    private String _2g_bands;
    private String _3g_bands;
    private String _4g_bands;

}
