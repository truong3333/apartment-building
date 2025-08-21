package com.example.apartment.repository;

import com.example.apartment.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,String> {
    List<Report> findAllByUsername(String username);
    List<Report> findAllByRoomNumber(String roomNumber);
    List<Report> findAllByStatus(String status);

    @Query("SELECT r FROM Report r " +
            "WHERE FUNCTION('MONTH', r.dateCreate) = :month " +
            "AND FUNCTION('YEAR', r.dateCreate) = :year")
    List<Report> findAllByMonthAndYear(@Param("month") int month,
                                                 @Param("year") int year);
}
