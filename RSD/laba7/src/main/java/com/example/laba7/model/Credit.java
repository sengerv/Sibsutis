package com.example.laba7.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Credit extends Payment {
    private String number;
    private String type;
    private LocalDate expDate;
}