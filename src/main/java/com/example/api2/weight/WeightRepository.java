package com.example.api2.weight;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeightRepository extends JpaRepository<Weight,String> {
    List<Weight> findAllByMemberIdOrderByRegDateDesc(String id);
}
