package com.example.apartment.controller;

import com.example.apartment.domain.dto.request.ApartmentHistoryRequest;
import com.example.apartment.domain.dto.response.ApartmentHistoryResponse;
import com.example.apartment.domain.dto.response.ApiResponses;
import com.example.apartment.service.ApartmentHistoryService;
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
@RequestMapping("/api/v1/apartment-history")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Tag(name = "ApartmentHistory", description = "Quản lý lịch sử cư dân căn hộ")
public class ApartmentHistoryController {
    ApartmentHistoryService apartmentHistoryService;

    @Operation(summary = "Tạo mới lịch sử căn hộ", description = "Tạo mới một lịch sử cư dân cho căn hộ")
    @ApiResponse(responseCode = "200", description = "Tạo lịch sử thành công")
    @PostMapping
    ApiResponses<String> createApartmentHistory(@RequestBody ApartmentHistoryRequest request){
        return ApiResponses.<String>builder()
                .result(apartmentHistoryService.create(request))
                .build();
    }

    @Operation(summary = "Lấy danh sách lịch sử căn hộ", description = "Lấy tất cả lịch sử cư dân của các căn hộ")
    @ApiResponse(responseCode = "200", description = "Danh sách lịch sử căn hộ")
    @GetMapping
    ApiResponses<List<ApartmentHistoryResponse>> getAllApartmentHistory(){
        return ApiResponses.<List<ApartmentHistoryResponse>>builder()
                .result(apartmentHistoryService.getAll())
                .build();
    }

    @Operation(summary = "Lấy danh sách lịch sử căn hộ", description = "Lấy tất cả lịch sử cư dân của các căn hộ")
    @ApiResponse(responseCode = "200", description = "Danh sách lịch sử căn hộ")
    @GetMapping("/{roomNumber}")
    ApiResponses<List<ApartmentHistoryResponse>> getAllApartmentHistoryByRoomnumber(@Parameter (description = "Số phòng") @PathVariable String roomNumber){
        return ApiResponses.<List<ApartmentHistoryResponse>>builder()
                .result(apartmentHistoryService.getAllByRoomnumber(roomNumber))
                .build();
    }

    @Operation(summary = "Lấy danh sách lịch sử căn hộ", description = "Lấy tất cả lịch sử cư dân của các căn hộ")
    @ApiResponse(responseCode = "200", description = "Danh sách lịch sử căn hộ")
    @GetMapping("/users/{username}")
    ApiResponses<List<ApartmentHistoryResponse>> getAllApartmentHistoryByUsername(@Parameter (description = "Username") @PathVariable String username){
        return ApiResponses.<List<ApartmentHistoryResponse>>builder()
                .result(apartmentHistoryService.getAllOfUsername(username))
                .build();
    }

    @Operation(summary = "Cập nhật lịch sử căn hộ", description = "Cập nhật thông tin lịch sử cư dân căn hộ")
    @ApiResponse(responseCode = "200", description = "Cập nhật lịch sử thành công")
    @PutMapping
    ApiResponses<String> deleteApartmentHistory(@RequestBody ApartmentHistoryRequest request){
        return ApiResponses.<String>builder()
                .result(apartmentHistoryService.update(request))
                .build();
    }
}