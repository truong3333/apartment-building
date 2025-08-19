package com.example.apartment.domain.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @Size(min = 8,message = "USERNAME_INVALID")
    String username;
    @Size(min = 8,message = "PASSWORD_INVALID")
    String password;
    String fullName;
    String email;
    String phone;
    String cmnd;
    String address;
    String gender;
    LocalDate dob;

}
