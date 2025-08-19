package com.example.apartment.repository;

import com.example.apartment.domain.entity.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CostRepository extends JpaRepository<Cost,String> {
    boolean existsByTypeAndMonthlyCost_Name(String type,String monthlyCost_Name);
    List<Cost> findAllByMonthlyCost_Name(String monthlyCost_Name);
    Optional<Cost> findByTypeAndMonthlyCost_Name(String type, String monthlyCost_Name);
}
