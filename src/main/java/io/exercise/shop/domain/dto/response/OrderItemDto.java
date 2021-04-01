package io.exercise.shop.domain.dto.response;

import io.exercise.shop.domain.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : choi-ys
 * @date : 2021/04/01 7:27 오후
 * @Content : 조회 Entity로부터 데이터를 설정하여 반환하기 위한 객체
 */
@Getter @Setter
public class OrderItemDto {

    private String itemName;
    private long orderPrice;
    private int orderCount;

    public OrderItemDto(OrderItem orderItem) {
        itemName = orderItem.getItem().getItemName();
        orderPrice = orderItem.getOrderPrice();
        orderCount = orderItem.getOrderCount();
    }
}