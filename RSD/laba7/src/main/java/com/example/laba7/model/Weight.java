package com.example.laba7.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;
import java.math.BigDecimal;

@Embeddable
@Data
public class Weight {
    private BigDecimal value;

    @Embedded
    private Measurement measurement;
}