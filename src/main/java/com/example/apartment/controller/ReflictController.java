package com.example.apartment.controller;

import com.example.apartment.domain.dto.request.ReflictRequest;
import com.example.apartment.domain.dto.request.ReflictUpdateRequest;
import com.example.apartment.domain.dto.response.ApiResponses;
import com.example.apartment.domain.dto.response.ReflictResponse;
import com.example.apartment.service.ReflictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reflict")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Tag(name = "Reflict", description = "Quản lý yêu cầu")
public class ReflictController {
    ReflictService reflictService;

    @Operation(summary = "Tạo yêu cầu ", description = "Tạo yêu cầu mới")
    @ApiResponse(responseCode = "200", description = "Tạo yêu cầu thành công")
    @PostMapping
    ApiResponses<String> createReflict(ReflictRequest request){
        return ApiResponses.<String>builder()
                .result(reflictService.create(request))
                .build();
    }

    @Operation(summary = "Xem danh sách tất cả yêu cầu ", description = "Lấy thông tin tất cả yêu cầu trong hệ thống")
    @ApiResponse(responseCode = "200", description = "Thông tin yêu cầu")
    @GetMapping
    ApiResponses<List<ReflictResponse>> getAllReflict(){
        return ApiResponses.<List<ReflictResponse>>builder()
                .result(reflictService.getAll())
                .build();
    }

    @Operation(summary = "Xem yêu cầu theo số phòng", description = "Lấy thông tin yêu cầu theo số phòng")
    @ApiResponse(responseCode = "200", description = "Thông tin yêu cầu")
    @GetMapping("/{roomNumber}")
    ApiResponses<List<ReflictResponse>> getAllByRoomnumber(@PathVariable String roomNumber){
        return ApiResponses.<List<ReflictResponse>>builder()
                .result(reflictService.getAllByRoomnumber(roomNumber))
                .build();
    }

    @Operation(summary = "Xem yêu cầu mình gửi", description = "Lấy thông tin yêu cầu mình đã gửi")
    @ApiResponse(responseCode = "200", description = "Thông tin yêu cầu")
    @GetMapping("/myReflict")
    ApiResponses<List<ReflictResponse>> getAllByUsername(){
        return ApiResponses.<List<ReflictResponse>>builder()
                .result(reflictService.getAllByUsername())
                .build();
    }

    @Operation(summary = "Cập nhật trạng thái yêu cầu ", description = "Cập nhật trạng thái yêu cầu ")
    @ApiResponse(responseCode = "200", description = "Cập nhật thành công")
    @PutMapping("/{reflictId}")
    ApiResponses<String> updateReflict(@PathVariable String reflictId, @RequestBody ReflictUpdateRequest request){
        return ApiResponses.<String>builder()
                .result(reflictService.update(reflictId, request))
                .build();
    }
}
