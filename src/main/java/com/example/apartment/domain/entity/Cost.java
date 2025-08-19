package com.example.apartment.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Cost {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String type;
    String description;
    Double amount;

    @ManyToOne
    @JoinColumn(name = "monthly_id")
    MonthlyCost monthlyCost;
}