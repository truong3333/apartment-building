package com.example.apartment.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticsResponse {

    long totalApartment;
    int apartmentUsage;
    double apartmentUsageRate;

    int userInSize;
    int userOutSize;
    List<ApartmentHistoryResponse> listUserIn = new ArrayList<>();
    List<ApartmentHistoryResponse> listUserOut = new ArrayList<>();

    long totalAmount;

    int totalReport;
    int reportStatusDone;
    double reportDoneRate;
}
