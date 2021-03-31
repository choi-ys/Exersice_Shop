package io.exercise.shop.repository.query;

import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.domain.entity.query.OrderCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/03/31 2:58 오후
 * @Content : 조회 조건에 해당하는 주문 조회
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager entityManager;

    /**
     * JPQL의 동적 쿼리를 이용한 조회 조건에 해당하는 주문 조회
     * @param orderCriteria {@link OrderCriteria}
     * @return List<Order>
     */
    public List<Order> findOrderByCriteria(OrderCriteria orderCriteria){
        String jpql = "SELECT o FROM Order o JOIN o.member m";
        boolean isFirstCondition = true;

        // 주문 상태 검색
        if (orderCriteria.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " WHERE";
                isFirstCondition = false;
            } else {
                jpql += " AND";
            }
            jpql += " o.orderStatus = :orderStatus";
        }
        // 회원 이름 'LIKE' 검색
        if (StringUtils.hasText(orderCriteria.getMemberName())) {
            if (isFirstCondition) {
                jpql += " WHERE";
                isFirstCondition = false;
            } else {
                jpql += " AND";
            }
            jpql += " m.memberName LIKE :memberName";
        }

        TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderCriteria.getOrderStatus() != null) {
            query = query.setParameter("orderStatus", orderCriteria.getOrderStatus());
        }

        if (StringUtils.hasText(orderCriteria.getMemberName())) {
            query = query.setParameter("memberName", orderCriteria.getMemberName());
        }

        return query.getResultList();
    }
}