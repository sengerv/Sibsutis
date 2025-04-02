package com.example.laba7.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

@Embeddable
@Data
public class Quantity {
    private Integer value;

    @Embedded
    private Measurement measurement;
}