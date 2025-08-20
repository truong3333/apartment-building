package com.example.apartment.service;

import com.example.apartment.domain.dto.request.ApartmentHistoryRequest;
import com.example.apartment.domain.dto.response.ApartmentHistoryResponse;
import com.example.apartment.domain.dto.response.UserResponse;
import com.example.apartment.domain.dto.response.UserResponseForAdmin;
import com.example.apartment.domain.entity.Apartment;
import com.example.apartment.domain.entity.ApartmentHistory;
import com.example.apartment.domain.entity.User;
import com.example.apartment.exception.AppException;
import com.example.apartment.exception.ErrorCode;
import com.example.apartment.repository.ApartmentHistoryRepository;
import com.example.apartment.repository.ApartmentRepository;
import com.example.apartment.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ApartmentHistoryService {
    ApartmentRepository apartmentRepository;
    UserRepository userRepository;
    ApartmentHistoryRepository apartmentHistoryRepository;

    @PreAuthorize("hasRole('ADMIN')")
//    @CacheEvict(value = "apartmentHistories", allEntries = true)
    public String create(ApartmentHistoryRequest request){

        Apartment apartment = apartmentRepository.findByRoomNumber(request.getRoomNumber()).orElseThrow(() -> {
            log.error("Apartment room number {} not exists, create resident to apartment failed.",request.getRoomNumber());
            throw new AppException(ErrorCode.APARTMENT_NOT_EXISTED);
        });

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
            log.error("Username: {} not exists, create resident to apartment failed.",request.getUsername());
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        });

        if (request.isRepresentative() && apartmentHistoryRepository.existsByApartment_RoomNumberAndIsRepresentativeTrue(request.getRoomNumber())) {
            log.error("Apartment room number: {} has a representative, create resident to apartment failed.", request.getRoomNumber());
            throw new AppException(ErrorCode.ISREPRESENTATIVE_EXISTED);
        }

        if(apartmentHistoryRepository.existsByApartment_RoomNumberAndUser_UsernameAndStatus(request.getRoomNumber(),request.getUsername(),"action")){
            log.error("Resident already existed in apartment, create apartment history failed.");
            throw new AppException(ErrorCode.APARTMENT_HISTORY_EXISTED);
        }

        ApartmentHistory apartmentHistory = ApartmentHistory.builder()
                .apartment(apartment)
                .user(user)
                .isRepresentative(request.isRepresentative())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status("action")
                .build();

        apartment.getListApartmentHistory().add(apartmentHistory);
        apartmentHistoryRepository.save(apartmentHistory);
        log.info("Create resident to the apartment: {} successfully.",request.getRoomNumber());

        return "Create resident to the apartment successfully.";
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @Cacheable(value = "apartmentHistories")
    public List<ApartmentHistoryResponse> getAll(){
        List<ApartmentHistory> listApartmentHistory = apartmentHistoryRepository.findAll();
        if(listApartmentHistory.isEmpty()){
            log.error("List apartment history is empty, get all apartment history failed.");
            throw new AppException(ErrorCode.APARTMENT_HISTORY_NULL);
        }

        log.info("Get all list apartment history successfully.");

        return listApartmentHistory.stream().map(apartmentHistory -> ApartmentHistoryResponse.builder()
                .roomNumber(apartmentHistory.getApartment().getRoomNumber())
                .userResponse(
                        UserResponseForAdmin.builder()
                                .username(apartmentHistory.getUser().getUsername())
                                .fullName(apartmentHistory.getUser().getUserDetail().getFullName())
                                .email(apartmentHistory.getUser().getUserDetail().getEmail())
                                .phone(apartmentHistory.getUser().getUserDetail().getPhone())
                                .cmnd(apartmentHistory.getUser().getUserDetail().getCmnd())
                                .address(apartmentHistory.getUser().getUserDetail().getAddress())
                                .gender(apartmentHistory.getUser().getUserDetail().getGender())
                                .dob(apartmentHistory.getUser().getUserDetail().getDob())
                                .build()
                )
                .isRepresentative(apartmentHistory.isRepresentative())
                .startDate(apartmentHistory.getStartDate())
                .endDate(apartmentHistory.getEndDate())
                .status(apartmentHistory.getStatus())
                .build()).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ApartmentHistoryResponse> getAllByRoomnumber(String roomNumber){
        List<ApartmentHistory> listApartmentHistory = apartmentHistoryRepository.findAllByApartment_roomNumber(roomNumber);

        log.info("Get all list apartment history successfully.");

        return listApartmentHistory.stream().map(apartmentHistory -> {
            return ApartmentHistoryResponse.builder()
                    .roomNumber(apartmentHistory.getApartment().getRoomNumber())
                    .userResponse(
                            UserResponseForAdmin.builder()
                                    .username(apartmentHistory.getUser().getUsername())
                                    .fullName(apartmentHistory.getUser().getUserDetail().getFullName())
                                    .email(apartmentHistory.getUser().getUserDetail().getEmail())
                                    .phone(apartmentHistory.getUser().getUserDetail().getPhone())
                                    .cmnd(apartmentHistory.getUser().getUserDetail().getCmnd())
                                    .address(apartmentHistory.getUser().getUserDetail().getAddress())
                                    .gender(apartmentHistory.getUser().getUserDetail().getGender())
                                    .dob(apartmentHistory.getUser().getUserDetail().getDob())
                                    .build()
                    )
                    .isRepresentative(apartmentHistory.isRepresentative())
                    .startDate(apartmentHistory.getStartDate())
                    .endDate(apartmentHistory.getEndDate())
                    .status(apartmentHistory.getStatus())
                    .build();
        }).toList();
    }

    @PreAuthorize("hasRole('RESIDENT')")
//    @Cacheable(value = "apartmentHistories")
    public List<ApartmentHistoryResponse> getAllOfUsername(String username){
        List<ApartmentHistory> listApartmentHistory = apartmentHistoryRepository.findAllByUser_Username(username);

        log.info("Get all list apartment history of user: {} successfully.",username);

        return listApartmentHistory.stream().map(apartmentHistory -> ApartmentHistoryResponse.builder()
                .roomNumber(apartmentHistory.getApartment().getRoomNumber())
                .userResponse(
                        UserResponseForAdmin.builder()
                                .username(apartmentHistory.getUser().getUsername())
                                .fullName(apartmentHistory.getUser().getUserDetail().getFullName())
                                .email(apartmentHistory.getUser().getUserDetail().getEmail())
                                .phone(apartmentHistory.getUser().getUserDetail().getPhone())
                                .cmnd(apartmentHistory.getUser().getUserDetail().getCmnd())
                                .address(apartmentHistory.getUser().getUserDetail().getAddress())
                                .gender(apartmentHistory.getUser().getUserDetail().getGender())
                                .dob(apartmentHistory.getUser().getUserDetail().getDob())
                                .build()
                )
                .isRepresentative(apartmentHistory.isRepresentative())
                .startDate(apartmentHistory.getStartDate())
                .endDate(apartmentHistory.getEndDate())
                .status(apartmentHistory.getStatus())
                .build()).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String update(ApartmentHistoryRequest request){
        ApartmentHistory apartmentHistory = apartmentHistoryRepository.findByApartment_RoomNumberAndUser_UsernameAndStatus(request.getRoomNumber(),request.getUsername(),"action").orElseThrow(() -> {
            log.error("Apartment history not existed, update failed.");
            throw new AppException(ErrorCode.APARTMENT_HISTORY_NOT_EXISTED);
        });

        if (!apartmentHistory.isRepresentative() && request.isRepresentative() && apartmentHistoryRepository.existsByApartment_RoomNumberAndIsRepresentativeTrue(request.getRoomNumber())) {
            log.error("Apartment room number: {} has a representative, update resident to apartment failed.", request.getRoomNumber());
            throw new AppException(ErrorCode.ISREPRESENTATIVE_EXISTED);
        }

        apartmentHistory.setRepresentative(request.isRepresentative());
        if(request.getEndDate() != null){
            apartmentHistory.setEndDate(request.getEndDate());
            apartmentHistory.setRepresentative(false);
            apartmentHistory.setStatus("leave");
        }

        apartmentHistoryRepository.save(apartmentHistory);
        log.info("Update apartment history for room number {} successfully.",apartmentHistory.getApartment().getRoomNumber());
        return "Update apartment history successfully";
    }
}