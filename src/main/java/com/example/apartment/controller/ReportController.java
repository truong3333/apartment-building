package com.example.apartment.controller;

import com.example.apartment.domain.dto.request.ReportRequest;
import com.example.apartment.domain.dto.request.ReportUpdateRequest;
import com.example.apartment.domain.dto.response.ApiResponses;
import com.example.apartment.domain.dto.response.ReportResponse;
import com.example.apartment.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequiredArgsConstructor
@RequestMapping("/report")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Tag(name = "Report", description = "Quản lý yêu cầu")
public class ReportController {
    ReportService reportService;

    @Operation(summary = "Tạo yêu cầu ", description = "Tạo yêu cầu mới")
    @ApiResponse(responseCode = "200", description = "Tạo yêu cầu thành công")
    @PostMapping
    ApiResponses<String> createReport(ReportRequest request){
        return ApiResponses.<String>builder()
                .result(reportService.create(request))
                .build();
    }

    @Operation(summary = "Xem danh sách tất cả yêu cầu ", description = "Lấy thông tin tất cả yêu cầu trong hệ thống")
    @ApiResponse(responseCode = "200", description = "Thông tin yêu cầu")
    @GetMapping
    ApiResponses<List<ReportResponse>> getAllReport(){
        return ApiResponses.<List<ReportResponse>>builder()
                .result(reportService.getAll())
                .build();
    }

    @Operation(summary = "Xem yêu cầu theo số phòng", description = "Lấy thông tin yêu cầu theo số phòng")
    @ApiResponse(responseCode = "200", description = "Thông tin yêu cầu")
    @GetMapping("/{roomNumber}")
    ApiResponses<List<ReportResponse>> getAllByRoomnumber(@PathVariable String roomNumber){
        return ApiResponses.<List<ReportResponse>>builder()
                .result(reportService.getAllByRoomnumber(roomNumber))
                .build();
    }

    @Operation(summary = "Xem yêu cầu mình gửi", description = "Lấy thông tin yêu cầu mình đã gửi")
    @ApiResponse(responseCode = "200", description = "Thông tin yêu cầu")
    @GetMapping("/myReport")
    ApiResponses<List<ReportResponse>> getAllByUsername(){
        return ApiResponses.<List<ReportResponse>>builder()
                .result(reportService.getAllByUsername())
                .build();
    }

    @Operation(summary = "Cập nhật trạng thái yêu cầu ", description = "Cập nhật trạng thái yêu cầu ")
    @ApiResponse(responseCode = "200", description = "Cập nhật thành công")
    @PutMapping("/{reportId}")
    ApiResponses<String> updateReport(@PathVariable String reportId, @RequestBody ReportUpdateRequest request){
        return ApiResponses.<String>builder()
                .result(reportService.update(reportId, request))
                .build();
    }
}
