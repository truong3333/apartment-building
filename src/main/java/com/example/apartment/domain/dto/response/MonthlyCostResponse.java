package com.example.apartment.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyCostResponse {

    String name;
    Double totalAmount;
    LocalDate dateCreate;
    String statusPayment;

    List<CostResponse> listCost = new ArrayList<>();
}