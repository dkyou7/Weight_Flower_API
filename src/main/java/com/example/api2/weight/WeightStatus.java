package com.example.api2.weight;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum WeightStatus {
    PLUS("증가"),MINUS("감소"),MAINTAIN("변동 없음"),FIRST("첫 등록");

    private final String status;

    public static List<WeightStatus> list(){
        return List.of(WeightStatus.values());
    }
}
