package com.example.apartment.repository;

import com.example.apartment.domain.entity.Reflict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReflictRepository extends JpaRepository<Reflict,String> {
    List<Reflict> findAllByUsername(String username);
    List<Reflict> findAllByRoomNumber(String roomNumber);
}
