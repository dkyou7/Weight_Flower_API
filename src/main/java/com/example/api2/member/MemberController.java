package com.example.api2.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 가입
     *
     * @param memberDTO 회원 가입에 필요한 객체 정보
     * @return 응답 객체
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody MemberDTO memberDTO) {
        return memberService.processSignUp(memberDTO);
    }

    /**
     * 로그인
     *
     * @param memberDTO 로그인에 필요한 객체 정보
     * @return 응답 객체
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody MemberDTO memberDTO) {
        return memberService.processSignIn(memberDTO);
    }

    /**
     * 닉네임 변경 + 등록
     *
     * @param email 이메일
     * @param memberDTO 변경에 필요한 닉네임 정보
     * @return 응답 객체
     */
    @PostMapping("/makeNickname")
    public ResponseEntity<?> makeNickname(@AuthenticationPrincipal String email, @RequestBody MemberDTO memberDTO) {
        return memberService.processUpdateNickname(email,memberDTO);
    }
}
