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
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String fullName;
    String email;
    String phone;
    String cmnd;
    String address;
    String gender;
    LocalDate dob;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
}
