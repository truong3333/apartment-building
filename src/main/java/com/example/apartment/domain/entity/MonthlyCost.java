package com.example.apartment.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyCost {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @JoinColumn(unique = true)
    String name;
    Double totalAmount;
    LocalDate dateCreate;
    String statusPayment;

    @OneToMany(mappedBy = "monthlyCost",cascade = CascadeType.ALL)
    List<Cost> listCost = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    Apartment apartment;
}
