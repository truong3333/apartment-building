package com.example.apartment.controller;

import com.example.apartment.domain.dto.response.ApiResponses;
import com.example.apartment.domain.dto.response.DashBoardResponse;
import com.example.apartment.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/dashboard")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DashBoardController {
    StatisticsService statisticsService;

    @Operation(summary = "Trang chủ", description = "Xem trang chủ")
    @ApiResponse(responseCode = "200", description = "Trang chủ")
    @PostMapping
    ApiResponses<DashBoardResponse> getDashBoard(){
        return ApiResponses.<DashBoardResponse>builder()
                .result(statisticsService.getDashBoard())
                .build();
    }
}
