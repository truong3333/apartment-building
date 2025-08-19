package com.example.apartment.repository;

import com.example.apartment.domain.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment,String> {
    boolean existsByRoomNumber(String roomNumber);
    Optional<Apartment> findByRoomNumber(String roomNumber);
}