package com.example.api2.weight;

import com.example.api2.dto.ResponseDTO;
import com.example.api2.member.Member;
import com.example.api2.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WeightService {

    private final MemberService memberService;
    private final WeightRepository weightRepository;

    /**
     * 몸무게 생성 프로세스
     *
     * @param dto    편지 DTO
     * @param email 사용자 이메일
     * @return 생성 이후 편지 Entity
     */
    public ResponseEntity<?> processCreate(WeightDTO dto, String email) {
        log.info("체중 등록 프로세스 시작합니다.");
        try {
            Weight weightEntity = MakeWeightEntity(dto,email);
            String savedMemberId = this.save(weightEntity);
            List<WeightDTO> data = retrieveDTO(savedMemberId);

            return returnOkRequest(data);
        } catch (Exception e) {
            return returnBadRequest(e);
        }
    }

    private Weight MakeWeightEntity(WeightDTO dto,String email) {
        Weight weightEntity = Weight.toEntity(dto);
        Member byEmail = memberService.findByEmail(email);
        weightEntity.setMember(byEmail);
        return weightEntity;
    }

    private String save(Weight weightEntity) {
        Weight save = weightRepository.save(weightEntity);

        return save.getMember().getId();
    }
    public List<Weight> retrieveAll(final String id) {
        return weightRepository.findAllByMemberIdOrderByRegDateDesc(id);
    }

    /**
     * 생성
     *
     * @param id 식별자 멤버 아이디
     * @return
     */
    public List<WeightDTO> retrieveDTO(final String id) {
        List<WeightDTO> data = new ArrayList<>();
        List<Weight> weight = weightRepository.findAllByMemberIdOrderByRegDateDesc(id);
        log.info("Weight 기록 수: " + weight.size());
        if(weight.size()>=1){
            Weight a = weight.get(0);
            if(weight.size()>1){
                Weight b = weight.get(1);
                a = setComment(a, b);
            }else{
                a.setStatus(WeightStatus.FIRST);
            }
            WeightDTO weightDTO = new WeightDTO(a);
            data.add(weightDTO);
        }
        return data;
    }

    private Weight setComment(Weight a, Weight b) {
        int compare = comp.compare(a, b);
        if(compare>0){
            a.setStatus(WeightStatus.PLUS);
        }else if(compare<0){
            a.setStatus(WeightStatus.MINUS);
        }else {
            a.setStatus(WeightStatus.MAINTAIN);
        }
        return a;
    }

    public static Comparator<Weight> comp = new Comparator<Weight>() {
        @Override
        public int compare(Weight o1, Weight o2) {
            return Float.compare(o1.getWeight(),o2.getWeight());
        }
    };
    private ResponseEntity<?> returnOkRequest(List<WeightDTO> data) {
         ResponseDTO<WeightDTO> response = ResponseDTO.<WeightDTO>builder().data(data).build();
         return ResponseEntity.ok().body(response);
        //return ResponseEntity.ok().body(data);
    }
    private ResponseEntity<?> returnBadRequest(Exception e) {
        ResponseDTO<WeightDTO> response = ResponseDTO.<WeightDTO>builder().error(e.toString()).build();
        return ResponseEntity.badRequest().body(response);
    }

    public ResponseEntity<?> processRetrieveWeightListByUserId(String userId) {
        try {
            Member byEmail = memberService.findByEmail(userId);
            List<Weight> list = weightRepository.findAllByMemberIdOrderByRegDateDesc(byEmail.getId());
            List<WeightDTO> data = list.stream().map(WeightDTO::new).collect(Collectors.toList());
            return returnOkRequest(data);
        } catch (Exception e) {
            return returnBadRequest(e);
        }
    }

    public ResponseEntity<?> processUpdate(WeightDTO dto, String userId) {
        try {
            Weight entity = WeightDTO.toEntity(dto);

            String update = this.update(entity);
            Weight byId = weightRepository.findById(update).orElseThrow(EntityNotFoundException::new);

            return ResponseEntity.ok().body(byId);
        } catch (Exception e) {
            return returnBadRequest(e);
        }
    }
    /**
     * 편지내용을 업데이트 한다.
     *
     * @param entity 편지 엔티티
     * @return saveId 수정된 편지 아이디
     */
    public String update(final Weight entity) {

        final Optional<Weight> original = retrieveWeight(entity.getId());

        original.ifPresent(el -> {
            el.setWeight(entity.getWeight());
            WeightStatus new_status = updateStatus(el.getMember().getId());
            el.setStatus(new_status);
            save(el);
        });

        return entity.getId();
    }

    private WeightStatus updateStatus(String memberId) {
        return retrieveDTO(memberId).get(0).getWeightStatus();
    }

    private Optional<Weight> retrieveWeight(String id) {
        return weightRepository.findById(id);
    }

    private List<Weight> findAllByMemberId(String memberId){
       return weightRepository.findAllByMemberIdOrderByRegDateDesc(memberId);
    }

    public ResponseEntity<?> processDelete(WeightDTO dto) {
        try {
            weightRepository.deleteById(dto.getId());
            return ResponseEntity.ok().body("삭제 성공");
        } catch (Exception e) {
            return returnBadRequest(e);
        }
    }

    public ResponseEntity<?> processReceiveDetail(String weightId) {
        try {
            Weight byLetterId = retrieveWeight(weightId).orElseThrow(EntityNotFoundException::new);
            return ResponseEntity.ok().body(byLetterId);
        } catch (Exception e) {
            return returnBadRequest(e);
        }
    }
}
