package com.example.apartment.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApartmentHistoryResponse {

    String roomNumber;
    UserResponseForAdmin userResponse;
    boolean isRepresentative;
    LocalDate startDate;
    LocalDate endDate;
    String status;

}
