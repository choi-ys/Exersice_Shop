package io.exercise.shop.service.query;

import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.domain.entity.query.OrderCriteria;
import io.exercise.shop.repository.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/03/31 6:58 오후
 * @Content : 주문 조회 관련 로직 처리 부
 *  - 조회 조건별 주문 조회
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;

    public List<Order> findOrderByCriteria(OrderCriteria orderCriteria){
        return orderQueryRepository.findOrderByCriteria(orderCriteria);
    }
}