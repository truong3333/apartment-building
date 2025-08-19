package com.example.apartment.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseForAdmin {
    String username;
    String fullName;
    String email;
    String phone;
    String cmnd;
    String address;
    String gender;
    LocalDate dob;

    Set<RoleResponse> roles;
}