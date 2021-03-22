package io.exercise.shop.repository;

import io.exercise.shop.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/03/22 10:45 오전
 * @Content : 회원 Entity와 회원 Table의 연동 처리 부
 */
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    /**
     * @PersistenceContext 어노테이션을 @Autowired로 대체[ 가능한 springboot-starter-data-jpa 의존성이 있는 경우,
     * lombok의 @RequiredArgsConstructor을 이용하여 EntityManager 주입 가능
     */
    private final EntityManager entityManager;

    public void create(Member member){
        entityManager.persist(member);
    }

    public Member find(long memberNo){
        return entityManager.find(Member.class, memberNo);
    }

    public void delete(Member member){
        entityManager.remove(member);
    }

    public List<Member> findMemberList(){
        return entityManager.createQuery("select member from Member as member").getResultList();
    }
}