package com.example.apartment.controller;

import com.example.apartment.domain.dto.request.ApartmentRequest;
import com.example.apartment.domain.dto.request.StatisticsRequest;
import com.example.apartment.domain.dto.response.ApartmentResponse;
import com.example.apartment.domain.dto.response.ApiResponses;
import com.example.apartment.domain.dto.response.StatisticsResponse;
import com.example.apartment.service.ApartmentService;
import com.example.apartment.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Tag(name = "Statistics", description = "Thống kê")
public class StatisticsController {
    StatisticsService statisticsService;

    @Operation(summary = "Xem thống kê", description = "Xem thống kê theo tháng")
    @ApiResponse(responseCode = "200", description = "Thông tin thống kê")
    @PostMapping
    ApiResponses<StatisticsResponse> getStatistics(@RequestBody StatisticsRequest request){
        return ApiResponses.<StatisticsResponse>builder()
                .result(statisticsService.getStatistics(request))
                .build();
    }

}