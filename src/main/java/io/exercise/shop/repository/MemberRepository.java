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

    /**
     * EntityManager를 이용한 객체의 영속상태 변경 : 비영속 -> 영속
     * 해당 EntityManager의 Transaction commit 시점에 쓰기 지연 저장소에 등록된 DML이 실행 되면서 영속화 된 객체가 DB에 저장
     * @param member     */
    public void save(Member member){
        entityManager.persist(member);
    }

    /**
     * PK를 이용한 특정 회원 조회
     * @param memberNo
     * @return
     */
    public Member findByMemberNo(long memberNo){
        return entityManager.find(Member.class, memberNo);
    }

    /**
     * 회원 Entity를 이용한 특정 회원 삭제
     * @param member
     */
    public void delete(Member member){
        entityManager.remove(member);
    }

    /** JQPL
     * 회원 목록 조회
     * @return
     */
    public List<Member> findAll(){
        return entityManager.createQuery("select m from Member as m", Member.class)
                .getResultList();
    }

    /** 파라미터 바인딩 JPQL
     * 특정 회원 이름을 가진 회원 목록 조회
     * @param memberName
     * @return
     */
    public List<Member> findByMemberName(String memberName){
        return entityManager.createQuery("select m from Member as m where m.memberName = :memberName", Member.class)
                .setParameter("memberName", memberName)
                .getResultList();
    }
}