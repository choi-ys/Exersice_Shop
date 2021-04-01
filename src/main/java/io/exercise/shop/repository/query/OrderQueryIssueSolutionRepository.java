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
}