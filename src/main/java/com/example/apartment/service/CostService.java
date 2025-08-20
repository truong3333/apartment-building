package com.example.apartment.service;

import com.example.apartment.domain.dto.request.CostRequest;
import com.example.apartment.domain.dto.response.CostResponse;
import com.example.apartment.domain.entity.Cost;
import com.example.apartment.domain.entity.MonthlyCost;
import com.example.apartment.exception.AppException;
import com.example.apartment.exception.ErrorCode;
import com.example.apartment.repository.CostRepository;
import com.example.apartment.repository.MonthlyCostRepository;
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
public class CostService {
    CostRepository costRepository;
    MonthlyCostRepository monthlyCostRepository;

    @PreAuthorize("hasRole('ADMIN')")
//    @CacheEvict(value = "costs", allEntries = true)
    public String create(CostRequest request){
        if(costRepository.existsByTypeAndMonthlyCost_Name(request.getType(),request.getMonthlyCost_Name())){
            log.error("Cost type: {} of monthly cost id: {} already existed, create failed",request.getType(),request.getMonthlyCost_Name());
            throw new AppException(ErrorCode.COST_EXISTED);
        }

        MonthlyCost monthlyCost = monthlyCostRepository.findByName(request.getMonthlyCost_Name()).orElseThrow(() -> {
            log.error("Monthly cost not existed,create cost failed.");
            throw new AppException(ErrorCode.MONTHLY_COST_NOT_EXISTED);
        });

        Cost cost = Cost.builder()
                .type(request.getType())
                .description(request.getDescription())
                .amount(request.getAmount())
                .monthlyCost(monthlyCost)
                .build();

        monthlyCost.getListCost().add(cost);
        costRepository.save(cost);
        monthlyCost.setTotalAmount(monthlyCost.getTotalAmount() + cost.getAmount());
        monthlyCostRepository.save(monthlyCost);

        log.info("Cost type: {} create successfully.",cost.getType());

        return "Cost type: " + cost.getType() + "create successfully.";
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @Cacheable(value = "costs", key = "#monthlyCost_name")
    public List<CostResponse> getAllByMonthly(String monthlyCost_name){
        List<Cost> listCost = costRepository.findAllByMonthlyCost_Name(monthlyCost_name);

        return listCost.stream().map(cost ->
                CostResponse.builder()
                        .id(cost.getId())
                        .type(cost.getType())
                        .description(cost.getDescription())
                        .amount(cost.getAmount())
                        .build()).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @CacheEvict(value = "costs", allEntries = true)
    public String update(CostRequest request){
        Cost cost = costRepository.findByTypeAndMonthlyCost_Name(request.getType(), request.getMonthlyCost_Name()).orElseThrow(() -> {
            log.error("Cost with type: {} not existed, update failed.",request.getType());
            throw new AppException(ErrorCode.COST_NOT_EXISTED);
        });

        MonthlyCost monthlyCost = monthlyCostRepository.findByName(request.getMonthlyCost_Name()).orElseThrow(() -> {
            log.error("Monthly cost not existed, update cost failed.");
            throw new AppException(ErrorCode.MONTHLY_COST_NOT_EXISTED);
        });

        monthlyCost.setTotalAmount(monthlyCost.getTotalAmount() - cost.getAmount());
        monthlyCost.getListCost().remove(cost);

        cost.setType(request.getType());
        cost.setDescription(request.getDescription());
        cost.setAmount(request.getAmount());

        monthlyCost.setTotalAmount(monthlyCost.getTotalAmount() + cost.getAmount());
        monthlyCost.getListCost().add(cost);
        costRepository.save(cost);
        log.info("Cost type: {} update successfully.",cost.getType());

        return "Cost updated successfully.";
    }

    public String delete(String cost_id){
        Cost cost = costRepository.findById(cost_id).orElseThrow(() -> {
            log.error("Cost id: {} not existed, delete failed.",cost_id);
            throw new AppException(ErrorCode.COST_NOT_EXISTED);
        });

        MonthlyCost monthlyCost = monthlyCostRepository.findByName(cost.getMonthlyCost().getName()).orElseThrow(() -> {
            log.error("Monthly cost not existed, delete cost failed.");
            throw new AppException(ErrorCode.MONTHLY_COST_NOT_EXISTED);
        });

        monthlyCost.setTotalAmount(monthlyCost.getTotalAmount() - cost.getAmount());
        monthlyCost.getListCost().remove(cost);

        costRepository.deleteById(cost.getId());
        log.info("Cost type: {} delete successfully.",cost.getType());

        return "Cost delete successfully.";
    }
}