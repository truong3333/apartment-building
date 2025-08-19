package com.example.apartment.controller;

import com.example.apartment.domain.dto.request.CostRequest;
import com.example.apartment.domain.dto.response.ApiResponses;
import com.example.apartment.domain.dto.response.CostResponse;
import com.example.apartment.service.CostService;
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
@RequestMapping("/api/v1/cost")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Cost", description = "Quản lý chi phí")
public class CostController {
    CostService costService;

    @Operation(summary = "Thêm chi phí mới", description = "Thêm mới chi phí")
    @ApiResponse(responseCode = "200", description = "Thêm chi phí thành công")
    @PostMapping
    ApiResponses<String> createCost(@RequestBody CostRequest request) {
        return ApiResponses.<String>builder()
                .code(1000)
                .result(costService.create(request))
                .build();
    }

    @Operation(summary = "Lấy danh sách chi phí tháng theo số phòng", description = "Lấy danh sách chi phí tháng chỉ định theo số phòng")
    @ApiResponse(responseCode = "200", description = "Danh sách chi phí tháng theo số phòng")
    @GetMapping("/{monthlyCost_name}")
    ApiResponses<List<CostResponse>> getAllByMonthly(@Parameter (description = "MonthlyCost_Name") @PathVariable String monthlyCost_name) {
        return ApiResponses.<List<CostResponse>>builder()
                .code(1000)
                .result(costService.getAllByMonthly(monthlyCost_name))
                .build();
    }

    @Operation(summary = "Sửa lại chi phí", description = "Sửa lại chi phí")
    @ApiResponse(responseCode = "200", description = "Sửa chi phí thành công")
    @PutMapping
    ApiResponses<String> updateCost(@RequestBody CostRequest request) {
        return ApiResponses.<String>builder()
                .code(1000)
                .result(costService.update(request))
                .build();
    }

    @Operation(summary = "Xoá chi phí", description = "Xoá chi phí")
    @ApiResponse(responseCode = "200", description = "Xoá chi phí thành công")
    @DeleteMapping("/{cost_id}")
    ApiResponses<String> deleteCost(@Parameter (description = "Cost_Id")@PathVariable String cost_id){
        return ApiResponses.<String>builder()
                .code(1000)
                .result(costService.delete(cost_id))
                .build();
    }
}