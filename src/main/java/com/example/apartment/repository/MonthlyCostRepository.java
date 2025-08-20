package com.example.apartment.repository;

import com.example.apartment.domain.entity.MonthlyCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonthlyCostRepository extends JpaRepository<MonthlyCost,String> {
    Optional<MonthlyCost> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT SUM(m.totalAmount) " +
            "FROM MonthlyCost m " +
            "WHERE m.name LIKE %:month% AND m.name LIKE %:year%")
    Long sumTotalAmountByMonthAndYear(@Param("month") int month,
                                      @Param("year") int year);
}