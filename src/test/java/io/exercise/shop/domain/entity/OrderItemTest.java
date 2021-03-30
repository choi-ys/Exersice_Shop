package io.exercise.shop.domain.entity;

import io.exercise.shop.domain.entity.item.Item;
import io.exercise.shop.generator.ItemGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author : choi-ys
 * @date : 2021/03/30 6:06 오후
 * @Content : 주문 상품 Entity Test
 */
@DisplayName("Entity:OrderItem")
class OrderItemTest {

    @Test
    @DisplayName("주문 상품 생성")
    public void createOrderItem(){
        // Given
        Item item = new ItemGenerator().buildBook();
        int orderCount = 2;
        // When
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(item.getItemPrice())
                .orderCount(orderCount)
                .build();

        // Then
        assertEquals(orderItem.getItem().getStockQuantity(), item.getStockQuantity());
        assertEquals(orderItem.getTotalOrderItemPrice(), item.getItemPrice() * orderCount);
    }

    @Test
    @DisplayName("주문 취소")
    public void cancel(){
        // Given
        Item item = new ItemGenerator().buildBook();
        int beforeStockCount = item.getStockQuantity();
        int orderCount = 2;

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(item.getItemPrice())
                .orderCount(orderCount)
                .build();
        assertEquals(orderItem.getItem().getStockQuantity(), item.getStockQuantity());
        assertEquals(orderItem.getTotalOrderItemPrice(), item.getItemPrice() * orderCount);

        // When
        orderItem.cancel();

        // Then
        assertEquals(item.getStockQuantity(), beforeStockCount);
    }
}