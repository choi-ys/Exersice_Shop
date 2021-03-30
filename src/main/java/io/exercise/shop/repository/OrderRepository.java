package io.exercise.shop.repository;

import io.exercise.shop.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * @author : choi-ys
 * @date : 2021/03/30 9:40 오후
 * @Content : 주문 Entity와 주문 Table의 연동 처리 부
 */
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager entityManager;

    /**
     * 신규 주문 생성
     * @param order 주문 정보
     */
    public void save(Order order){
        entityManager.persist(order);
    }

    /**
     * 주문 조회
     * @param orderNo 조회 주문 번호
     * @return 조회 주문 정보
     */
    public Order findByOrderNo(long orderNo){
        return entityManager.find(Order.class, orderNo);
    }
}