package com.example.apartment.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ApartmentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    boolean isRepresentative;
    LocalDate startDate;
    LocalDate endDate;
    String status;

}
