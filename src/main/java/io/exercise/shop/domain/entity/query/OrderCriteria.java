package io.exercise.shop.domain.entity.query;

import io.exercise.shop.domain.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : choi-ys
 * @date : 2021/03/31 3:01 오후
 * @Content : 주문 조회 조건에 해당하는 파라미터
 */
@Getter @Setter
public class OrderCriteria {

    private String memberName;

    private OrderStatus orderStatus;
}