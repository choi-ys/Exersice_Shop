package io.exercise.shop.repository;

import io.exercise.shop.domain.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author : choi-ys
 * @date : 2021/03/22 10:51 오전
 * @Content : 회원 Repository Test Case
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Repository:MemberRepository")
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("Create:Member")
    @Transactional
    @Rollback(value = false)
    public void create(){
        // Given
        String memberName = "최용석";
        Member member = Member.builder()
                .memberName(memberName)
                .build();

        // When
        this.memberRepository.create(member);

        // Then
        assertEquals(member, memberRepository.find(member.getMemberNo()));
    }
}