package io.exercise.shop.service;

import io.exercise.shop.domain.entity.Member;
import io.exercise.shop.generator.MemberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author : choi-ys
 * @date : 2021/03/23 9:34 오전
 * @Content : 회원 Service Test Case
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Service:MemberService")
@Import(MemberGenerator.class)
@Transactional
class MemberServiceTest {

    @Resource
    MemberGenerator memberGenerator;

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원 가입")
//    @Rollback(value = false)
    public void join(){
        // Given
        Member member = memberGenerator.buildMember();

        // When
        long joinedMemberNo = this.memberService.join(member);

        // Then
        assertThat(joinedMemberNo).isNotZero();
    }

    @Test
    @DisplayName("회원 가입 중복 검사 예외 처리")
//    @Rollback(value = false)
    public void duplicatedMemberJoin(){
        // Given
        Member member = memberGenerator.buildMember();
        this.memberService.join(member);

        // When
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> {
            this.memberService.join(member);
        });

        // Then
        assertThat(illegalStateException.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @Test
    @DisplayName("회원 조회")
    public void findMemberByMemberNo(){
        // Given
        Member member = this.memberGenerator.buildMember();
        long joinedMemberNo = this.memberService.join(member);

        // When
        entityManager.flush();
        Member joinedMember = this.memberService.findMember(joinedMemberNo);

        // Then : 동일 트랙잭션 안에서 객체 동일성 보장
        assertEquals(member, joinedMember);
    }

    @Test
    @DisplayName("회원 목록 조회")
    public void findAllMember(){
        // Given
        Member firstMember = this.memberGenerator.buildMemberByMemberName("최용석");
        Member secondMember = this.memberGenerator.buildMemberByMemberName("이성욱");
        Member thirdMember = this.memberGenerator.buildMemberByMemberName("박재현");

        this.memberService.join(firstMember);
        this.memberService.join(secondMember);
        this.memberService.join(thirdMember);

        // When
        List<Member> memberList = this.memberService.findMemberList();

        // Then
        assertThat(memberList).contains(firstMember);
        assertThat(memberList).contains(secondMember);
        assertThat(memberList).contains(thirdMember);
    }
}
