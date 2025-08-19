package com.example.apartment.controller;

import com.example.apartment.domain.dto.request.ApartmentRequest;
import com.example.apartment.domain.dto.response.ApartmentResponse;
import com.example.apartment.domain.dto.response.ApiResponses;
import com.example.apartment.service.ApartmentService;
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
@RequestMapping("/api/v1/apartment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Tag(name = "Apartment", description = "Quản lý căn hộ")
public class ApartmentController {
    ApartmentService apartmentService;

    @Operation(summary = "Tạo mới căn hộ", description = "Tạo mới một căn hộ với thông tin truyền vào")
    @ApiResponse(responseCode = "200", description = "Tạo căn hộ thành công")
    @PostMapping
    ApiResponses<String> createApartment(@RequestBody ApartmentRequest request){
        return ApiResponses.<String>builder()
                .result(apartmentService.create(request))
                .build();
    }

    @Operation(summary = "Xem căn hộ theo số phòng", description = "Lấy thông tin căn hộ trong hệ thống")
    @ApiResponse(responseCode = "200", description = "Thông tin căn hộ")
    @GetMapping("/{roomNumber}")
    ApiResponses<ApartmentResponse> getApartmentByRoomNumber(@Parameter(description = "Số phòng") @PathVariable String roomNumber){
        return ApiResponses.<ApartmentResponse>builder()
                .result(apartmentService.getByRoomNumber(roomNumber))
                .build();
    }

    @Operation(summary = "Xem căn hộ theo username", description = "Lấy thông tin căn hộ trong hệ thống")
    @ApiResponse(responseCode = "200", description = "Thông tin căn hộ")
    @GetMapping("/myApartment")
    ApiResponses<List<ApartmentResponse>> getApartmentByUsername(){
        return ApiResponses.<List<ApartmentResponse>>builder()
                .result(apartmentService.getByUserName())
                .build();
    }

    @Operation(summary = "Lấy danh sách căn hộ", description = "Lấy tất cả căn hộ trong hệ thống")
    @ApiResponse(responseCode = "200", description = "Danh sách căn hộ")
    @GetMapping
    ApiResponses<List<ApartmentResponse>> getAllApartment(){
        return ApiResponses.<List<ApartmentResponse>>builder()
                .result(apartmentService.getAll())
                .build();
    }

    @Operation(summary = "Xóa căn hộ", description = "Xóa căn hộ theo số phòng")
    @ApiResponse(responseCode = "200", description = "Xóa căn hộ thành công")
    @DeleteMapping("/{roomNumber}")
    ApiResponses<String> deleteApartment(@Parameter(description = "Số phòng") @PathVariable String roomNumber){
        return ApiResponses.<String>builder()
                .result(apartmentService.delete(roomNumber))
                .build();
    }

}