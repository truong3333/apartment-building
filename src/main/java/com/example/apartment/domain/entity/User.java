package com.example.apartment.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    String password;

    @OneToMany(mappedBy = "user")
    List<ApartmentHistory> listApartmentHistory = new ArrayList<>();

    @ManyToMany
    Set<Role> roles;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    UserDetail userDetail;

}