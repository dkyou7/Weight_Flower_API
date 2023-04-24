package com.example.api2.weight;

import lombok.Data;

@Data
public class MainWeightDto {
    private String nickname;
    private String status;

    public MainWeightDto(String status, String nickname) {
        this.nickname = nickname;
        this.status =  status;
    }
}
