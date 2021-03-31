package io.exercise.shop.domain.entity;

import io.exercise.shop.domain.entity.item.Item;
import io.exercise.shop.exception.NotEnoughStockException;
import io.exercise.shop.generator.ItemGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("상품 재고 수량을 초과하는 품목 생성")
    public void createOrderItem_NotEnoughStockException(){
        Item item = new ItemGenerator().buildBook();
        int orderCount = item.getStockQuantity() + 1;

        // When & Then : 주문 생성 시 재고 부족 예외 발생
        NotEnoughStockException notEnoughStockException = assertThrows(NotEnoughStockException.class, () -> {
            OrderItem.builder()
                    .item(item)
                    .orderPrice(item.getItemPrice())
                    .orderCount(orderCount)
                    .build();
        });
        assertEquals(notEnoughStockException.getMessage(), "재고가 부족합니다.");
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