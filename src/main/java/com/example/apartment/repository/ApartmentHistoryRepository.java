package com.example.apartment.repository;

import com.example.apartment.domain.entity.ApartmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentHistoryRepository extends JpaRepository<ApartmentHistory,String> {
    List<ApartmentHistory> findAllByApartment_roomNumber(String roomNumber);
    List<ApartmentHistory> findAllByUser_Username(String username);
    List<ApartmentHistory> findAllByUser_UsernameAndStatus(String roomNumber,String status);
    List<ApartmentHistory> findAllByIsRepresentativeTrue();
    boolean existsByApartment_RoomNumberAndIsRepresentativeTrue(String roomNumber);
    boolean existsByApartment_RoomNumberAndUser_UsernameAndStatus(String roomnumber,String username,String status);
    Optional<ApartmentHistory> findByApartment_RoomNumberAndUser_Username(String roomNumber,String username);
    Optional<ApartmentHistory> findByApartment_RoomNumberAndIsRepresentativeTrue(String roomNumber);

}
