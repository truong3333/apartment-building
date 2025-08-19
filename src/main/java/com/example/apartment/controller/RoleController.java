package com.example.apartment.controller;

import com.example.apartment.domain.dto.response.ApiResponses;
import com.example.apartment.domain.dto.response.RoleResponse;
import com.example.apartment.service.RoleService;
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
@RequestMapping("/api/v1/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Role", description = "Quản lý vai trò người dùng")
public class RoleController {
    RoleService roleService;

    @Operation(summary = "Lấy danh sách vai trò", description = "Lấy tất cả vai trò trong hệ thống")
    @ApiResponse(responseCode = "200", description = "Danh sách role")
    @GetMapping
    ApiResponses<List<RoleResponse>> getAll() {
        return ApiResponses.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }


}