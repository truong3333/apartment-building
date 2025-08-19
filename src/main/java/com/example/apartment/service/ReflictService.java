package com.example.apartment.service;

import com.example.apartment.domain.dto.request.ReflictRequest;
import com.example.apartment.domain.dto.request.ReflictUpdateRequest;
import com.example.apartment.domain.dto.response.ReflictResponse;
import com.example.apartment.domain.entity.ApartmentHistory;
import com.example.apartment.domain.entity.Reflict;
import com.example.apartment.exception.AppException;
import com.example.apartment.exception.ErrorCode;
import com.example.apartment.repository.ApartmentHistoryRepository;
import com.example.apartment.repository.ReflictRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ReflictService {
    ReflictRepository reflictRepository;
    ApartmentHistoryRepository apartmentHistoryRepository;

    @PreAuthorize("hasRole('RESIDENT')")
    public String create(ReflictRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!apartmentHistoryRepository.existsByApartment_RoomNumberAndUser_UsernameAndStatus(request.getRoomNumber(),username,"action")){
            log.error("You are not in this room or have left before.");
            throw new AppException(ErrorCode.NOT_IN_APARTMENT);
        }

        log.info("Reflict of user: {} create successfully", username);
        Reflict reflict = Reflict.builder()
                .username(username)
                .roomNumber(request.getRoomNumber())
                .description(request.getDescription())
                .createDate(LocalDate.now())
                .endDate(null)
                .status("wait")
                .build();

        reflictRepository.save(reflict);

        return "Reflict of user: " + username + " create successfully";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReflictResponse> getAll(){
        List<Reflict> listReflict = new ArrayList<>(reflictRepository.findAll());

        log.info("Get all reflict successfully");
        return listReflict.stream().map(reflict -> ReflictResponse.builder()
                .username(reflict.getUsername())
                .roomNumber(reflict.getRoomNumber())
                .description(reflict.getDescription())
                .createDate(reflict.getCreateDate())
                .endDate(reflict.getEndDate())
                .status(reflict.getStatus())
                .build()
        ).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReflictResponse> getAllByRoomnumber(String roomNumber){
        List<Reflict> listReflict = new ArrayList<>(reflictRepository.findAllByRoomNumber(roomNumber));

        log.info("Get all reflict of room number: {} successfully",roomNumber);
        return listReflict.stream().map(reflict -> ReflictResponse.builder()
                .username(reflict.getUsername())
                .roomNumber(reflict.getRoomNumber())
                .description(reflict.getDescription())
                .createDate(reflict.getCreateDate())
                .endDate(reflict.getEndDate())
                .status(reflict.getStatus())
                .build()
        ).toList();
    }

    @PreAuthorize("hasRole('RESIDENT')")
    public List<ReflictResponse> getAllByUsername(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Reflict> listReflict = new ArrayList<>(reflictRepository.findAllByUsername(username));

        log.info("Get all reflict of user: {} successfully",username);
        return listReflict.stream().map(reflict -> ReflictResponse.builder()
                .username(reflict.getUsername())
                .roomNumber(reflict.getRoomNumber())
                .description(reflict.getDescription())
                .createDate(reflict.getCreateDate())
                .endDate(reflict.getEndDate())
                .status(reflict.getStatus())
                .build()
        ).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String update(String reflictId, ReflictUpdateRequest request){
        Reflict reflict = reflictRepository.findById(reflictId).orElseThrow(() -> {
            log.error("Reflict not exists, update reflict failed");
            throw new AppException(ErrorCode.REFLICT_NOT_EXISTED);
        });

        if(request.getEndDate() != null){
            reflict.setEndDate(request.getEndDate());
            reflict.setStatus("done");
        }

        reflictRepository.save(reflict);

        log.info("Update reflict id: {} successfully",reflictId);
        return "Update reflict id: " + reflictId + " successfully";
    }
}
