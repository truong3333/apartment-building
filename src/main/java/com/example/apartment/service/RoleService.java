package com.example.apartment.service;

import com.example.apartment.domain.dto.response.RoleResponse;
import com.example.apartment.domain.entity.Role;
import com.example.apartment.exception.AppException;
import com.example.apartment.exception.ErrorCode;
import com.example.apartment.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAll(){
        List<Role> roles = roleRepository.findAll();
        if(roles.isEmpty()){
            log.error("No role found in the system,getAll role failed.");
            throw new AppException(ErrorCode.ROLE_NULL);
        }

        log.info("Get all Role successfully.");
        return roles.stream().map(role -> RoleResponse.builder()
                    .name(role.getName())
                    .description(role.getDescription())
                    .build()
        ).toList();
    }
}
