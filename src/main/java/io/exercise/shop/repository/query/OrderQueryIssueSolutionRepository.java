package io.exercise.shop.repository.query;

import io.exercise.shop.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryIssueSolutionRepository {

    private final EntityManager entityManager;

    public List<Order> findAll(){
        return entityManager.createQuery("select o from Order as o")
                .getResultList();
    }

    /**
     * Entity 조회 시 N:1, 1:1의 관계를 FETCH JOIN으로 조회
     * @return
     */
    public List<Order> findByFetchJoin(){
        return entityManager.createQuery("select o from Order as o" +
                " join fetch o.member as m" +
                " join fetch o.delivery as d")
                .getResultList();
    }
}