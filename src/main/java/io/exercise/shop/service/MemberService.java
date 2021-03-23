package io.exercise.shop.service;

import io.exercise.shop.domain.entity.Member;
import io.exercise.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/03/23 9:20 오전
 * @Content : 회원 관련 로직 처리 부
 *  - 회원 가입
 *  - 특정 회원 조회
 *  - 회원 목록 조회
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     *  - 회원 이름을 이용한 중복 유효성 검사
     * @param member
     * @return
     *  - success : memberNo
     *  - fail : IllegalStateException
     */
    public long join(Member member){
       validateDuplicatedMember(member);
       this.memberRepository.save(member);
       return member.getMemberNo();
    }

    /**
     * 회원 이름을 이용한 중복 가입 검사 부
     * @param member
     */
    private void validateDuplicatedMember(Member member) {
        int memberCount = this.memberRepository.findByMemberName(member.getMemberName()).size();
        if(memberCount > 0){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 특정 회원 조회
     * @param memberNo
     */
    public Member findMember(long memberNo){
        return memberRepository.findByMemberNo(memberNo);
    }

    /**
     * 회원 목록 조회
     * @return List<Member>
     */
    public List<Member> findMemberList(){
        return this.memberRepository.findAll();
    }
}