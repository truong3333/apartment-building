package com.example.apartment.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Apartment {
    @Id
    String roomNumber;
    Double area;
    String address;

    @OneToMany(mappedBy = "apartment",cascade = CascadeType.ALL)
    List<ApartmentHistory> listApartmentHistory = new ArrayList<>();

    @OneToMany(mappedBy = "apartment",cascade = CascadeType.ALL)
    List<MonthlyCost> listMonthlyCost = new ArrayList<>();

}
