package com.example.api2.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String token;
    private String password;
    private String email;
    private String username;
    private String id;
    private String nickname;        // 카카오 프로필 닉네임
    private LocalDate regDate;

    public MemberDTO(Member member){
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
