package io.exercise.shop.repository;

import io.exercise.shop.domain.entity.Address;
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
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author : choi-ys
 * @date : 2021/03/22 10:51 오전
 * @Content : 회원 Repository Test Case
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Repository:MemberRepository")
@Import(MemberGenerator.class)
@Transactional
class MemberRepositoryTest {

    @Resource
    MemberGenerator memberGenerator;

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("Insert:Member")
    @Rollback(value = false)
    public void create(){
        // Given
        Member member = this.memberGenerator.buildMember();

        // When
        this.memberRepository.save(member);

        // Then
        assertEquals(member, memberRepository.findByMemberNo(member.getMemberNo()));
    }

    @Test
    @DisplayName("Select:Member")
    @Rollback(value = false)
    @Transactional(readOnly = true)
    public void findByMemberNo(){
        // Given
        Member member = this.memberGenerator.buildMember();
        this.memberRepository.save(member);

        // When
        Member savedMember = this.memberRepository.findByMemberNo(member.getMemberNo());

        // Then : 동일 트랜잭션에서 객체의 동일성 보장
        assertEquals(member, savedMember);
    }

    @Test
    @DisplayName("Update:Member")
    public void update(){
        // Given
        Member member = this.memberGenerator.buildMember();
        this.memberRepository.save(member);

        // When
        Address address = new Address("서울특별시", "서초구 주흥길 23", "06539");
        member.setAddress(address);

        // Then : Persist Context에 종속된 객체의 값 변경 시 변경 감지로 인한 update문 출력 여부 확인
        entityManager.flush();
        Member updatedMember = this.memberRepository.findByMemberNo(member.getMemberNo());
        assertEquals(address, updatedMember.getAddress());
    }

    @Test
    @DisplayName("Delete:Member")
    @Rollback(value = false)
    public void newTest(){
        // Given
        Member member = this.memberGenerator.buildMember();
        this.memberRepository.save(member);

        // When
        this.memberRepository.delete(member);

        // Then
        assertThat(this.memberRepository.findByMemberNo(member.getMemberNo())).isNull();
    }

    @Test
    @DisplayName("Select:MemberList")
    @Rollback(value = false)
    public void findAll(){
        // Given
        Member firstMember = this.memberGenerator.buildMemberByMemberName("최용석");
        Member secondMember = this.memberGenerator.buildMemberByMemberName("이성욱");
        Member thirdMember = this.memberGenerator.buildMemberByMemberName("박재현");

        this.memberRepository.save(firstMember);
        this.memberRepository.save(secondMember);
        this.memberRepository.save(thirdMember);

        // When : JPQL실행 시점에 flush 발생 -> 쓰기 지연 저장소의 SQL 실행
        List<Member> memberList = this.memberRepository.findAll();

        // Then
        assertThat(memberList).contains(firstMember);
        assertThat(memberList).contains(secondMember);
        assertThat(memberList).contains(thirdMember);
    }
}