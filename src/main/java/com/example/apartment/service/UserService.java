package com.example.apartment.service;

import com.example.apartment.domain.dto.response.RoleResponse;
import com.example.apartment.domain.dto.response.UserResponseForAdmin;
import com.example.apartment.domain.entity.Role;
import com.example.apartment.domain.entity.UserDetail;
import com.example.apartment.domain.dto.request.UserCreateRequest;
import com.example.apartment.domain.dto.request.UserUpdateRequest;
import com.example.apartment.domain.dto.response.UserResponse;
import com.example.apartment.domain.entity.User;
import com.example.apartment.exception.AppException;
import com.example.apartment.exception.ErrorCode;
import com.example.apartment.repository.RoleRepository;
import com.example.apartment.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    //    @CacheEvict(value = "users", key = "#result.username", condition = "#result != null")
    public UserResponse createUser(UserCreateRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            log.error("User with username {} already exists, create failed!", request.getUsername());
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Role role = roleRepository.findByName("RESIDENT").orElseThrow(() -> {
            log.error("No role found in the system, create user failed.");
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        });

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);

        UserDetail userDetail = new UserDetail();
        userDetail.setEmail(request.getEmail());
        userDetail.setFullName(request.getFullName());
        userDetail.setPhone(request.getPhone());
        userDetail.setDob(request.getDob());
        userDetail.setGender(request.getGender());
        userDetail.setCmnd(request.getCmnd());
        userDetail.setAddress(request.getAddress());

        user.setUserDetail(userDetail);
        userDetail.setUser(user);

        userRepository.save(user);

        log.info("User {} created successfully!", user.getUsername());

        return UserResponse.builder()
                .username(user.getUsername())
                .fullName(userDetail.getFullName())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseForAdmin> getAllUsers(){
        List<User> listAllUsers = userRepository.findAll();
        if (listAllUsers.isEmpty()) {
            log.warn("No users found in the system.");
            throw new AppException(ErrorCode.LIST_USER_NULL);
        }

        log.info("Admin has retrieved the list of all users");
        return listAllUsers.stream()
                .map(user ->
                {
                    UserDetail userDetail = user.getUserDetail();
                    return UserResponseForAdmin.builder()
                            .username(user.getUsername())
                            .roles(user.getRoles().stream().map(role ->
                                    RoleResponse.builder()
                                            .name(role.getName())
                                            .description(role.getDescription())
                                            .build()
                            ).collect(Collectors.toSet()))
                            .fullName(userDetail.getFullName())
                            .email(userDetail.getEmail())
                            .phone(userDetail.getPhone())
                            .cmnd(userDetail.getCmnd())
                            .address(userDetail.getAddress())
                            .gender(userDetail.getGender())
                            .dob(userDetail.getDob())
                            .build();
                })
                .toList();
    }

    @PreAuthorize("hasAnyRole('RESIDENT','ADMIN')")
    public UserResponseForAdmin getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> {
            log.error("Username: {} not found, get info failed.", name);
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        });

        UserDetail userDetail = user.getUserDetail();

        return UserResponseForAdmin.builder()
                .username(user.getUsername())
                .roles(user.getRoles().stream().map(role ->
                        RoleResponse.builder()
                                .name(role.getName())
                                .description(role.getDescription())
                                .build()
                ).collect(Collectors.toSet()))
                .fullName(userDetail.getFullName())
                .email(userDetail.getEmail())
                .phone(userDetail.getPhone())
                .cmnd(userDetail.getCmnd())
                .address(userDetail.getAddress())
                .gender(userDetail.getGender())
                .dob(userDetail.getDob())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @Cacheable(value = "users", key = "#username")
    public UserResponseForAdmin getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        UserDetail userDetail = user.getUserDetail();

        return UserResponseForAdmin.builder()
                .username(user.getUsername())
                .roles(user.getRoles().stream().map(role ->
                        RoleResponse.builder()
                                .name(role.getName())
                                .description(role.getDescription())
                                .build()
                ).collect(Collectors.toSet()))
                .fullName(userDetail.getFullName())
                .email(userDetail.getEmail())
                .phone(userDetail.getPhone())
                .cmnd(userDetail.getCmnd())
                .address(userDetail.getAddress())
                .gender(userDetail.getGender())
                .dob(userDetail.getDob())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @CacheEvict(value = "users", key = "#userId")
    public String updateUser(String username,UserUpdateRequest request){


        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("User with ID: {} not found, update User failed!", username);
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        });

//        var roles = roleRepository.findAllById(request.getRoles());
        UserDetail userDetail = user.getUserDetail();

        if(request.getPassword() != null){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        } else{
            user.setPassword(user.getPassword());
        }

//        user.setRoles(new HashSet<>(roles));
        userDetail.setEmail(request.getEmail());
        userDetail.setFullName(request.getFullName());
        userDetail.setPhone(request.getPhone());
        userDetail.setGender(request.getGender());
        userDetail.setDob(request.getDob());
        userDetail.setCmnd(request.getCmnd());
        userDetail.setAddress(request.getAddress());

        user.setUserDetail(userDetail);
        userDetail.setUser(user);

        userRepository.save(user);
        log.info("User with ID: {} updated successfully!", username);

        return "Update username: " + " username successfully";
    }

}