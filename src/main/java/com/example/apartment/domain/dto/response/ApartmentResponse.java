package com.example.apartment.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApartmentResponse {
    String roomNumber;
    Double area;
    String address;

    List<ApartmentHistoryResponse> listApartmentHistory;
    List<MonthlyCostResponse> listMonthlyCost;
}
