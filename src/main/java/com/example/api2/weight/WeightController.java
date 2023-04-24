package com.example.api2.weight;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/weight")
public class WeightController {

    private final WeightService weightService;

    /**
     * 체중 리스트
     *
     * @return 사용자 체중 리스트
     */
    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        return weightService.processFindAll();
    }

    /**
     * 상세 조회 기능
     *
     * @param weightId 체중 고유 식별자
     * @return 상세 조회 데이터
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> receiveLetter(@PathVariable("id") String weightId) {
        return weightService.processReceiveDetail(weightId);
    }

    /**
     * 체중 리스트
     *
     * @param email 이메일
     * @return 사용자 체중 리스트
     */
    @GetMapping
    public ResponseEntity<?> retrieveWeightListByUserId(@AuthenticationPrincipal String email) {
        return weightService.processRetrieveWeightListByUserId(email);
    }

    /**
     * 체중 등록
     *
     * @param dto 체중 등록 정보
     * @param email 이메일
     * @return 등록 이후 응답 객체
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody WeightDTO dto,
                                    @AuthenticationPrincipal String email) {
        return weightService.processCreate(dto, email);
    }

    /**
     * 체중 수정
     *
     * @param dto 수정 객체
     * @param email 이메일
     * @return 등록 이후 응답 객체
     */
    @PutMapping
    public ResponseEntity<?> updateWeight(@RequestBody WeightDTO dto,
                                          @AuthenticationPrincipal String email) {
        return weightService.processUpdate(dto, email);
    }

    /**
     * 체중 삭제
     *
     * @param dto 삭제 하고자 하는 체중 식별 아이디
     * @return 등록 이후 응답 객체
     */
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody WeightDTO dto) {
        return weightService.processDelete(dto);
    }
}
