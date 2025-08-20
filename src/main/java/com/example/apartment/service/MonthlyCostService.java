package com.example.apartment.service;

import com.example.apartment.domain.dto.request.MailRequest;
import com.example.apartment.domain.dto.request.MonthlyCostRequest;
import com.example.apartment.domain.dto.request.MonthlyCostUpdateRequest;
import com.example.apartment.domain.dto.response.CostResponse;
import com.example.apartment.domain.dto.response.MonthlyCostResponse;
import com.example.apartment.domain.entity.*;
import com.example.apartment.exception.AppException;
import com.example.apartment.exception.ErrorCode;
import com.example.apartment.repository.ApartmentHistoryRepository;
import com.example.apartment.repository.ApartmentRepository;
import com.example.apartment.repository.CostRepository;
import com.example.apartment.repository.MonthlyCostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
public class MonthlyCostService {
    ApartmentRepository apartmentRepository;
    CostRepository costRepository;
    MonthlyCostRepository monthlyCostRepository;
    ApartmentHistoryRepository apartmentHistoryRepository;
    JavaMailSender mailSender;

    @PreAuthorize("hasRole('ADMIN')")
//    @CacheEvict(value = "monthlyCosts", allEntries = true)
    public String create(MonthlyCostRequest request){
        String name = request.getRoomNumber() + "-" + request.getMonth() + "-" + request.getYear();
        if(monthlyCostRepository.existsByName(name)){
            log.error("Monthly cost name: {} already exists,create failed.",name);
            throw new AppException(ErrorCode.MONTHLY_COST_EXISTED);
        }

        Apartment apartment = apartmentRepository.findByRoomNumber(request.getRoomNumber()).orElseThrow(() -> {
            log.error("Apartment room number : {} not exists, create monthly cost failed.",request.getRoomNumber());
            throw new AppException(ErrorCode.APARTMENT_NOT_EXISTED);
        });

        List<Cost> listCost = costRepository.findAllByMonthlyCost_Name(name);

        MonthlyCost monthlyCost = MonthlyCost.builder()
                .name(name)
                .totalAmount(listCost.stream().mapToDouble(Cost::getAmount).sum())
                .dateCreate(LocalDate.now())
                .statusPayment("wait")
                .listCost(listCost)
                .apartment(apartment)
                .build();

        apartment.getListMonthlyCost().add(monthlyCost);
        monthlyCostRepository.save(monthlyCost);
        log.info("Monthly cost of room number: {} create successfully.",request.getRoomNumber());

        return "Monthly cost create successfully";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<MonthlyCostResponse> getAll(){
        List<MonthlyCost> listMonthlyCost = new ArrayList<>(monthlyCostRepository.findAll());

        return listMonthlyCost.stream().map(monthlyCost ->
                MonthlyCostResponse.builder()
                        .name(monthlyCost.getName())
                        .totalAmount(monthlyCost.getTotalAmount())
                        .dateCreate(monthlyCost.getDateCreate())
                        .statusPayment(monthlyCost.getStatusPayment())
                        .listCost(monthlyCost.getListCost().stream().map(cost ->
                                CostResponse.builder()
                                        .id(cost.getId())
                                        .type(cost.getType())
                                        .description(cost.getDescription())
                                        .amount(cost.getAmount())
                                        .build()).toList())
                        .build()).toList();

    }

    @PreAuthorize("hasRole('ADMIN')")
//    @Cacheable(value = "monthlyCosts", key = "#roomNumber")
    public List<MonthlyCostResponse> getAllByRoomNumber(String roomNumber) {
        Apartment apartment = apartmentRepository.findByRoomNumber(roomNumber).orElseThrow(() -> {
            log.error("Apartment room number : {} not exists, get monthly cost failed.", roomNumber);
            throw new AppException(ErrorCode.APARTMENT_NOT_EXISTED);
        });

        List<MonthlyCost> listMonthlyCost = apartment.getListMonthlyCost();

        return listMonthlyCost.stream().map(monthlyCost ->
                MonthlyCostResponse.builder()
                        .name(monthlyCost.getName())
                        .totalAmount(monthlyCost.getTotalAmount())
                        .dateCreate(monthlyCost.getDateCreate())
                        .statusPayment(monthlyCost.getStatusPayment())
                        .listCost(monthlyCost.getListCost().stream().map(cost ->
                                CostResponse.builder()
                                        .id(cost.getId())
                                        .type(cost.getType())
                                        .description(cost.getDescription())
                                        .amount(cost.getAmount())
                                        .build()).toList())
                        .build()).toList();
    }

    @PreAuthorize("hasRole('RESIDENT')")
    public List<MonthlyCostResponse> getByUsername(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<ApartmentHistory> listApartmentHistory = new ArrayList<>(apartmentHistoryRepository.findAllByUser_UsernameAndStatus(username,"action"));
        List<MonthlyCost> listMonthlyCost= new ArrayList<>();
        for(ApartmentHistory ah : listApartmentHistory){
            listMonthlyCost.addAll(ah.getApartment().getListMonthlyCost());
        }

        return listMonthlyCost.stream().map(monthlyCost ->
                MonthlyCostResponse.builder()
                        .name(monthlyCost.getName())
                        .totalAmount(monthlyCost.getTotalAmount())
                        .dateCreate(monthlyCost.getDateCreate())
                        .statusPayment(monthlyCost.getStatusPayment())
                        .listCost(monthlyCost.getListCost().stream().map(cost ->
                                CostResponse.builder()
                                        .id(cost.getId())
                                        .type(cost.getType())
                                        .description(cost.getDescription())
                                        .amount(cost.getAmount())
                                        .build()).toList())
                        .build()).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @CacheEvict(value = "monthlyCosts", allEntries = true)
    public String update(MonthlyCostUpdateRequest request) {
        MonthlyCost monthlyCost = monthlyCostRepository.findByName(request.getName()).orElseThrow(() -> {
            log.error("Monthly cost with name: {} not exists, update failed.", request.getName());
            throw new AppException(ErrorCode.MONTHLY_COST_NOT_EXISTED);
        });

        monthlyCost.setStatusPayment(request.getStatusPayment());

        monthlyCostRepository.save(monthlyCost);
        log.info("Monthly cost with name: {} updated successfully.", request.getName());

        return "Monthly cost updated successfully";
    }


    //---------------------------------------------------------------------------------------
    // Send email

    @PreAuthorize("hasRole('ADMIN')")
    public void sendMonthlyCost(MailRequest request) {
        String name = request.getRoomNumber() + "-" + request.getMonth() + "-" + request.getYear();

        ApartmentHistory apartmentHistory = apartmentHistoryRepository.findByApartment_RoomNumberAndIsRepresentativeTrue(request.getRoomNumber()).orElseThrow(() -> {
            log.error("Apartment: {} not exists representative, send email monthly cost failed");
            throw new AppException(ErrorCode.ISREPRESENTATIVE_NOT_EXISTED);
        });

        MonthlyCost monthlyCost = monthlyCostRepository.findByName(name).orElseThrow(() -> {
                log.error("Monthly cost with name: {} not exists, send email monthly cost failed.",name);
                throw new AppException(ErrorCode.MONTHLY_COST_NOT_EXISTED);
        });

        StringBuilder content = new StringBuilder("Xin chào " + apartmentHistory.getUser().getUserDetail().getFullName() + " phòng " + request.getRoomNumber() + ".\n\n");
        content.append("Ban quản lý chung cư xin thông báo chi phí tháng " + request.getMonth() + " năm " + request.getYear() + " của bạn bao gồm:\n\n");

        for (Cost cost : monthlyCost.getListCost()) {
            content.append("- Loại: ").append(cost.getType())
                    .append("  -  ").append(cost.getDescription())
                    .append("  -  ").append(cost.getAmount()).append(" VND\n");
        }
        content.append("\nTổng cộng: ").append(monthlyCost.getTotalAmount()).append(" VND\n\n");
        content.append("Mong cư dân thu xếp thời gian và thanh toán đúng hạn cho ban quan lý, xin cảm ơn!");

        // Gửi email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(apartmentHistory.getUser().getUserDetail().getEmail());
        message.setSubject("Báo cáo chi phí tháng");
        message.setText(content.toString());

        mailSender.send(message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void sendMonthlyCostAllUsers(MailRequest request) {
        List<ApartmentHistory> listApartmentHistory = apartmentHistoryRepository.findAllByIsRepresentativeTrue();

        for (ApartmentHistory ah : listApartmentHistory) {
            if (ah.getUser().getUserDetail().getEmail() != null && !ah.getUser().getUserDetail().getEmail().isEmpty()) {
                try {
                    request.setRoomNumber(ah.getApartment().getRoomNumber());
                    sendMonthlyCost(request);
                } catch (Exception e) {
                    // log lỗi nếu gửi thất bại
                    System.err.println("Không gửi được email cho user: " + ah.getUser().getUsername());
                }
            }
        }
    }

}
