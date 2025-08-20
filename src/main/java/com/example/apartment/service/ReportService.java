package com.example.apartment.service;

import com.example.apartment.domain.dto.request.ReportRequest;
import com.example.apartment.domain.dto.request.ReportUpdateRequest;
import com.example.apartment.domain.dto.response.ReportResponse;
import com.example.apartment.domain.entity.Report;
import com.example.apartment.exception.AppException;
import com.example.apartment.exception.ErrorCode;
import com.example.apartment.repository.ApartmentHistoryRepository;
import com.example.apartment.repository.ReportRepository;
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
public class ReportService {
    ReportRepository reportRepository;
    ApartmentHistoryRepository apartmentHistoryRepository;

    @PreAuthorize("hasRole('RESIDENT')")
    public String create(ReportRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!apartmentHistoryRepository.existsByApartment_RoomNumberAndUser_UsernameAndStatus(request.getRoomNumber(),username,"action")){
            log.error("You are not in this room or have left before.");
            throw new AppException(ErrorCode.NOT_IN_APARTMENT);
        }

        log.info("Reflict of user: {} create successfully", username);
        Report report = Report.builder()
                .username(username)
                .roomNumber(request.getRoomNumber())
                .description(request.getDescription())
                .dateCreate(LocalDate.now())
                .dateEnd(null)
                .status("wait")
                .build();

        reportRepository.save(report);

        return "Reflict of user: " + username + " create successfully";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReportResponse> getAll(){
        List<Report> listReport = new ArrayList<>(reportRepository.findAll());

        log.info("Get all reflict successfully");
        return listReport.stream().map(report -> ReportResponse.builder()
                .id(report.getId())
                .username(report.getUsername())
                .roomNumber(report.getRoomNumber())
                .description(report.getDescription())
                .dateCreate(report.getDateCreate())
                .dateEnd(report.getDateEnd())
                .status(report.getStatus())
                .build()
        ).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReportResponse> getAllByRoomnumber(String roomNumber){
        List<Report> listReport = new ArrayList<>(reportRepository.findAllByRoomNumber(roomNumber));

        log.info("Get all reflict of room number: {} successfully",roomNumber);
        return listReport.stream().map(report -> ReportResponse.builder()
                .id(report.getId())
                .username(report.getUsername())
                .roomNumber(report.getRoomNumber())
                .description(report.getDescription())
                .dateCreate(report.getDateCreate())
                .dateEnd(report.getDateEnd())
                .status(report.getStatus())
                .build()
        ).toList();
    }

    @PreAuthorize("hasRole('RESIDENT')")
    public List<ReportResponse> getAllByUsername(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Report> listReport = new ArrayList<>(reportRepository.findAllByUsername(username));

        log.info("Get all reflict of user: {} successfully",username);
        return listReport.stream().map(report -> ReportResponse.builder()
                .id(report.getId())
                .username(report.getUsername())
                .roomNumber(report.getRoomNumber())
                .description(report.getDescription())
                .dateCreate(report.getDateCreate())
                .dateEnd(report.getDateEnd())
                .status(report.getStatus())
                .build()
        ).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String update(String reflictId, ReportUpdateRequest request){
        Report report = reportRepository.findById(reflictId).orElseThrow(() -> {
            log.error("Reflict not exists, update reflict failed");
            throw new AppException(ErrorCode.REPORT_NOT_EXISTED);
        });

        if(request.getDateEnd() != null){
            report.setDateEnd(request.getDateEnd());
            report.setStatus("done");
        }

        reportRepository.save(report);

        log.info("Update reflict id: {} successfully",reflictId);
        return "Update reflict id: " + reflictId + " successfully";
    }
}
