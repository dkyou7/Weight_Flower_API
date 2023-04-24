package com.example.api2.weight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WeightRepository extends JpaRepository<Weight,String> {
    List<Weight> findAllByMemberIdOrderByRegDateDesc(String id);

    @Query(value =
            "SELECT w.status as status, w.member.nickname as nickname " +
            "FROM Weight w " +
            "where w.regDate >= :today " +
            "GROUP BY w.status"
    )
    List<mainWeight> findAllGroupByStatus(LocalDate today);
}
