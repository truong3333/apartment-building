package com.example.apartment.repository;

import com.example.apartment.domain.entity.MonthlyCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyCostRepository extends JpaRepository<MonthlyCost,String> {
    Optional<MonthlyCost> findByName(String name);
    boolean existsByName(String name);
    List<MonthlyCost> findAllByApartment_RoomNumber(String roomNumber);
}