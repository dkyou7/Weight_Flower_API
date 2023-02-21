package com.example.api2.member;

import com.example.api2.dto.ResponseDTO;
import com.example.api2.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public Member create(final Member member) {
        memberCreateValidation(member);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        log.info("회원 가입 일자 : " + member.getRegDate());
        return memberRepository.save(member);
    }

    private void memberCreateValidation(Member member) {
        if (checkInvalidArg(member)) {
            throw new RuntimeException("Invalid arguments");
        }
        if (existByEmail(member.getEmail())) {
            log.warn("Eamil already exists {}", member.getEmail());
            throw new RuntimeException("Email already exists");
        }
    }

    private boolean checkInvalidArg(Member member) {
        return member == null || member.getEmail() == null;
    }

    public Member getByCredentials(final String email, final String password) {
        Member byEmail = memberRepository.findByEmail(email);
        if (!passwordEncoder.matches(password, byEmail.getPassword())) {
            log.warn("password not matched {}", email);
            throw new RuntimeException("password not matched");
        }
        return byEmail;
    }

    public boolean existByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Long getMemberCount() {
        return memberRepository.count();
    }

    private LocalDate strToLocalDateTime(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public ResponseEntity<?> processUpdateNickname(String email, MemberDTO memberDTO) {
        try {
            Member byEmail = this.findByEmail(email);
            byEmail.setNickname(memberDTO.getNickname());

            return returnOkRequest(new MemberDTO(byEmail));
        } catch (Exception e) {
            return returnBadRequest(e);
        }

    }

    private ResponseEntity<?> returnOkRequest(MemberDTO data) {
//        ResponseDTO<MemberDTO> response = ResponseDTO.<MemberDTO>builder().data(data).build();
//        return ResponseEntity.ok().body(response);
        return ResponseEntity.ok().body(data);
    }
    private ResponseEntity<?> returnBadRequest(Exception e) {
        ResponseDTO<MemberDTO> response = ResponseDTO.<MemberDTO>builder().error(e.toString()).build();
        return ResponseEntity.badRequest().body(response);
    }


    public ResponseEntity<?> processSignUp(MemberDTO memberDTO) {
        try {
            Member member = Member.builder().email(memberDTO.getEmail())
                    .username(memberDTO.getUsername())
                    .password(memberDTO.getPassword())
                    .regDate(LocalDate.now())
                    .build();

            Member registeredUser = this.create(member);

            MemberDTO responseUserDTO = MemberDTO.builder()
                    .email(registeredUser.getEmail())
                    .username(registeredUser.getUsername())
                    .regDate(member.getRegDate())
                    .id(registeredUser.getId()).build();

            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    public ResponseEntity<?> processSignIn(MemberDTO memberDTO) {
        Member member = this.getByCredentials(memberDTO.getEmail(), memberDTO.getPassword());
        if (member != null) {
            final String token = tokenProvider.create(member);
            final MemberDTO responseUserDTO = MemberDTO.builder()
                    .email(member.getEmail())
                    .username(member.getUsername())
                    .id(member.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error("login failed").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
