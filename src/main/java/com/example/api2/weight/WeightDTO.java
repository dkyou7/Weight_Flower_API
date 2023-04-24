package com.example.api2.weight;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeightDTO {
    private String id;
    private Float weight;
    private LocalDate regDate;
    private WeightStatus weightStatus;

    public WeightDTO(Weight weight){
        this.id = weight.getId();
        this.weight = weight.getWeight();
        this.regDate = weight.getRegDate();
        this.weightStatus = weight.getStatus();
    }

    public static Weight toEntity(WeightDTO dto) {
        return Weight.builder().id(dto.getId()).weight(dto.getWeight()).regDate(LocalDate.now()).build();
    }
}
