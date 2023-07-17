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
public class ReturnItemResponse
{

    private LocalDateTime returnDate;
    private Long itemId;
    private String brand;
    private String model;
    /*private String technology;
    private String _2g_bands;
    private String _3g_bands;
    private String _4g_bands;*/
    private boolean available;

}
