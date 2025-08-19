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
public class UserUpdateRequest {

    @Size(min = 8,message = "PASSWORD_INVALID")
    String password;
    String oldPassword;
    String fullName;
    String email;
    String phone;
    String cmnd;
    String address;
    String gender;
    LocalDate dob;

}
