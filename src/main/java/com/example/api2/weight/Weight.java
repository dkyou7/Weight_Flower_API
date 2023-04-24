package com.example.api2.weight;

import com.example.api2.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Weight {

    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "uuid")
    private String id;

    private Float weight;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDate regDate; // 생성 시간 지정

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "member_id")
    private Member member;

    private WeightStatus status;

    public static Weight toEntity(WeightDTO dto) {
        return Weight.builder().weight(dto.getWeight()).regDate(LocalDate.now()).build();
    }
}
