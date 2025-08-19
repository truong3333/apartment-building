package com.example.apartment.domain.dto.response;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReflictResponse {

    String username;
    String roomNumber;
    String description;
    LocalDate createDate;
    LocalDate endDate;
    String status;
}
