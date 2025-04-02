package com.example.laba7.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Measurement {
    private String name;
}