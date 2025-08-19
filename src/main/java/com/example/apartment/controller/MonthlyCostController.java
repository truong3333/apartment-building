package com.example.apartment.controller;

import com.example.apartment.domain.dto.request.MailRequest;
import com.example.apartment.domain.dto.request.MonthlyCostRequest;
import com.example.apartment.domain.dto.request.MonthlyCostUpdateRequest;
import com.example.apartment.domain.dto.response.ApiResponses;
import com.example.apartment.domain.dto.response.MonthlyCostResponse;
import com.example.apartment.service.MonthlyCostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/monthly-cost")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Tag(name = "Monthly-Cost", description = "Quản lý chi phí hàng tháng")

public class MonthlyCostController {
    MonthlyCostService monthlyCostService;

    @PostMapping
    ApiResponses<String> createMonthlyCost(@RequestBody MonthlyCostRequest request) {
        return ApiResponses.<String>builder()
                .code(1000)
                .result(monthlyCostService.create(request))
                .build();
    }

    @GetMapping("/{roomNumber}")
    ApiResponses<List<MonthlyCostResponse>> getAllByRoomNumber(@Parameter(description = "Room number")@PathVariable String roomNumber) {
        return ApiResponses.<List<MonthlyCostResponse>>builder()
                .code(1000)
                .result(monthlyCostService.getAllByRoomNumber(roomNumber))
                .build();
    }

    @GetMapping("/myMonthlyCost")
    ApiResponses<List<MonthlyCostResponse>> getAllByUsername(@Parameter(description = "Username")@PathVariable String username) {
        return ApiResponses.<List<MonthlyCostResponse>>builder()
                .code(1000)
                .result(monthlyCostService.getAllByRoomNumber(username))
                .build();
    }

    @PutMapping
    ApiResponses<String> updateMonthlyCost(@RequestBody MonthlyCostUpdateRequest request) {
        return ApiResponses.<String>builder()
                .code(1000)
                .result(monthlyCostService.update(request))
                .build();
    }

    //-----------------------------------------------------------------------------------------------------
    @PostMapping("/sendEmail")
    ApiResponses<String> sendEmail(@RequestBody MailRequest request) {
        monthlyCostService.sendMonthlyCost(request);
        return ApiResponses.<String>builder()
                .code(1000)
                .result("Send email monthly cost of all apartment successfully")
                .build();
    }

    @PostMapping("/sendAllEmail")
    ApiResponses<String> sendAllEmail(@RequestBody MailRequest request) {
        monthlyCostService.sendMonthlyCostAllUsers(request);
        return ApiResponses.<String>builder()
                .code(1000)
                .result("Send email monthly cost of all apartment successfully")
                .build();
    }


}