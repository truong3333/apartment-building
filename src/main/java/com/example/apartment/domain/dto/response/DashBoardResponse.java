package com.example.apartment.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashBoardResponse {

    long totalApartment;
    int apartmentUsage;
    double apartmentUsageRate;

    int residentAction;

    int reportWait;
}
