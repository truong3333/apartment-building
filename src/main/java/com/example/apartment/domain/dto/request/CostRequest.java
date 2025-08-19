package com.example.apartment.domain.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CostRequest {
    String type;
    String description;
    Double amount;
    String monthlyCost_Name;
}
