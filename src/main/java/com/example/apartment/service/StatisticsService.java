package com.example.apartment.service;

import com.example.apartment.domain.dto.request.StatisticsRequest;
import com.example.apartment.domain.dto.response.DashBoardResponse;
import com.example.apartment.domain.dto.response.StatisticsResponse;
import com.example.apartment.domain.dto.response.UserResponseForAdmin;
import com.example.apartment.domain.entity.ApartmentHistory;
import com.example.apartment.domain.entity.Report;
import com.example.apartment.repository.ApartmentHistoryRepository;
import com.example.apartment.repository.ApartmentRepository;
import com.example.apartment.repository.MonthlyCostRepository;
import com.example.apartment.repository.ReportRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class StatisticsService {
    ApartmentRepository apartmentRepository;
    ApartmentHistoryRepository apartmentHistoryRepository;
    MonthlyCostRepository monthlyCostRepository;
    ReportRepository reportRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public StatisticsResponse getStatistics(StatisticsRequest request){
        Set<String> userIn = new HashSet<>();
        Set<String> userOut = new HashSet<>();
        Set<String> roomUsage = new HashSet<>();

        int countReportDone = 0;

        List<UserResponseForAdmin> listUserIn = new ArrayList<>();
        List<UserResponseForAdmin> listUserOut = new ArrayList<>();

        List<ApartmentHistory> listApartmentHistory = new ArrayList<>(apartmentHistoryRepository.findAllByMonthAndYear(request.getMonth(), request.getYear()));
        for(ApartmentHistory ah : listApartmentHistory){
            userIn.add(ah.getUser().getUsername());

            UserResponseForAdmin userInResponse = UserResponseForAdmin.builder()
                    .username(ah.getUser().getUsername())
                    .fullName(ah.getUser().getUserDetail().getFullName())
                    .email(ah.getUser().getUserDetail().getEmail())
                    .phone(ah.getUser().getUserDetail().getPhone())
                    .cmnd(ah.getUser().getUserDetail().getCmnd())
                    .address(ah.getUser().getUserDetail().getAddress())
                    .gender(ah.getUser().getUserDetail().getGender())
                    .dob(ah.getUser().getUserDetail().getDob())
                    .build();

            listUserIn.add(userInResponse);

            if(ah.getEndDate() != null && ah.getEndDate().getMonthValue() == request.getMonth()){
                userOut.add(ah.getUser().getUsername());

                UserResponseForAdmin userOutResponse = UserResponseForAdmin.builder()
                        .username(ah.getUser().getUsername())
                        .fullName(ah.getUser().getUserDetail().getFullName())
                        .email(ah.getUser().getUserDetail().getEmail())
                        .phone(ah.getUser().getUserDetail().getPhone())
                        .cmnd(ah.getUser().getUserDetail().getCmnd())
                        .address(ah.getUser().getUserDetail().getAddress())
                        .gender(ah.getUser().getUserDetail().getGender())
                        .dob(ah.getUser().getUserDetail().getDob())
                        .build();

                listUserOut.add(userOutResponse);
            }

        }

        List<ApartmentHistory> listApartmentHistoryAll = apartmentHistoryRepository.findAll();

        for(ApartmentHistory ah1 : listApartmentHistoryAll){
            if(ah1.getEndDate() == null){
                roomUsage.add(ah1.getApartment().getRoomNumber());
            }else{
                if(ah1.getEndDate().getMonthValue() >= request.getMonth())
                    roomUsage.add(ah1.getApartment().getRoomNumber());
            }
        }

        List<Report> listReport = new ArrayList<>(reportRepository.findAllByMonthAndYear(request.getMonth(), request.getYear()));

        for(Report report : listReport){
            if(report.getStatus().equals("done"))
                countReportDone++ ;
        }

        log.info("Get statistics of month {} year {} successfully",request.getMonth(),request.getYear());

        return StatisticsResponse.builder()
                .totalApartment(apartmentRepository.count())
                .apartmentUsage(roomUsage.size())
                .apartmentUsageRate(((double) (roomUsage.size()/apartmentHistoryRepository.count()) * 10000.0) /100.0)
                .userInSize(userIn.size())
                .userOutSize(userOut.size())
                .listUserIn(listUserIn)
                .listUserOut(listUserOut)
                .totalAmount(monthlyCostRepository.sumTotalAmountByMonthAndYear(request.getMonth(), request.getYear()))
                .totalReport(listReport.size())
                .reportStatusDone(countReportDone)
                .reportDoneRate(((double) (countReportDone/listReport.size()) * 10000.0) / 100.0)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN)")
    public DashBoardResponse getDashBoard(){
        StatisticsRequest request = StatisticsRequest.builder()
                .month(LocalDate.now().getMonthValue())
                .year(LocalDate.now().getYear())
                .build();
        StatisticsResponse statisticsResponse = getStatistics(request);

        return DashBoardResponse.builder()
                .totalApartment(statisticsResponse.getTotalApartment())
                .apartmentUsage(statisticsResponse.getApartmentUsage())
                .apartmentUsageRate(statisticsResponse.getApartmentUsageRate())
                .residentAction(apartmentHistoryRepository.countResidentAction())
                .reportWait(statisticsResponse.getTotalReport() - statisticsResponse.getReportStatusDone())
                .build();
    }
}
