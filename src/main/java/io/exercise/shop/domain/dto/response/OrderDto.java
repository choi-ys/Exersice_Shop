package io.exercise.shop.domain.dto.response;

import io.exercise.shop.domain.entity.Address;
import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.domain.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author : choi-ys
 * @date : 2021/04/01 7:26 오후
 * @Content : 조회 Entity로부터 데이터를 설정하여 반환하기 위한 객체
 */
@Getter @Setter
public class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
        orderId = order.getOrderNo();
        name = order.getMember().getMemberName();
        orderDate = order.getOrderDate();
        orderStatus = order.getOrderStatus();
        address = order.getDelivery().getAddress();
        orderItems = order.getOrderItemList().stream()
                .map(orderItem -> new OrderItemDto(orderItem))
                .collect(toList());
    }
}