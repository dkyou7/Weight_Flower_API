package com.example.api2.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oAuthService;

    /**
     * 카카오 callback
     * [GET] /oauth/kakao/callback
     *
     * @param code 카카오에서 리턴해주는 코드
     * @return 응답 객체
     */
    @ResponseBody
    @GetMapping("/kakao")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        return oAuthService.processCallback(code);
    }

    /**
     * 카카오 callback 이후 로직
     * 서비스 회원 가입 및 access_token 발급
     *
     * @param token 카카오에서 제공하는 토큰
     * @return 응답 객체
     */
    @ResponseBody
    @GetMapping("/accessToken")
    public ResponseEntity<?> kakaoAccessToken(@RequestParam String token) {
        return oAuthService.processAfterKakao(token);
    }
}