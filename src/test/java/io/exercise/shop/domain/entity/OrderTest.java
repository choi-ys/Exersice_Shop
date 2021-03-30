package io.exercise.shop.domain.entity;

import io.exercise.shop.domain.entity.item.Item;
import io.exercise.shop.generator.DeliveryGenerator;
import io.exercise.shop.generator.ItemGenerator;
import io.exercise.shop.generator.MemberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author : choi-ys
 * @date : 2021/03/30 5:29 오후
 * @Content : 주문 Entity Test
 */
@DisplayName("Entity:Order")
class OrderTest {

    @Test
    @DisplayName("신규 주문 생성")
    public void createOrder(){
        // Given : 주문 회원 정보
        Member member = new MemberGenerator().buildMember();

        // Given : 배송 정보
        Delivery delivery = new DeliveryGenerator().buildDelivery();

        // Given : 상품 정보
        Item item = new ItemGenerator().buildBook();
        int beforeStockCount = item.getStockQuantity(); // 주문전 상품 재고
        int orderCount = 2; // 주문 수량

        // Given : 주문 상품 정보
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(item.getItemPrice())
                .orderCount(orderCount)
                .build();
        OrderItem[] orderItems = new OrderItem[]{orderItem};


        // When : 신규 주문 생성
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItems(orderItems)
                .build();

        // Then : 양방향 연관관계를 가진 객체의 값 동기화 여부 확인
        assertThat(member.getOrderList().contains(order));
        assertEquals(delivery.getOrder(), order);
        assertEquals(orderItem.getOrder(), order);

        // Then : 주문 생성 시, 주문 수량만큼 상품의 재고 감소 여부 확인
        assertEquals(item.getStockQuantity() + orderCount, beforeStockCount);
    }

    @Test
    @DisplayName("주문 취소")
    public void cancel(){
        // Given : 주문 회원 정보
        Member member = new MemberGenerator().buildMember();

        // Given : 배송 정보
        Delivery delivery = new DeliveryGenerator().buildDelivery();

        // Given : 상품 정보
        Item item = new ItemGenerator().buildBook();
        int beforeStockCount = item.getStockQuantity(); // 주문전 상품 재고
        int orderCount = 2; // 주문 수량

        // Given : 주문 상품 정보
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(item.getItemPrice())
                .orderCount(orderCount)
                .build();

        OrderItem[] orderItems = new OrderItem[]{orderItem};

        // When : 신규 주문 생성
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItems(orderItems)
                .build();

        // When: 주문 취소
        order.cancel();

        // Then : 주문 수량만큼 주문 상품의 재고 추가 여부 확인
        assertEquals(item.getStockQuantity(), beforeStockCount);
    }

    @Test
    @DisplayName("주문 취소 : 배송 완료의 주문 취소 예외")
    public void orderCancel_ComplateOrder(){
        // Given : 주문 회원 정보
        Member member = new MemberGenerator().buildMember();

        // Given : 배송 정보
        Delivery delivery = new DeliveryGenerator().buildDelivery();

        // Given : 상품 정보
        Item item = new ItemGenerator().buildBook();
        int beforeStockCount = item.getStockQuantity(); // 주문전 상품 재고
        int orderCount = 2; // 주문 수량

        // Given : 주문 상품 정보
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(item.getItemPrice())
                .orderCount(orderCount)
                .build();
        OrderItem[] orderItems = new OrderItem[]{orderItem};

        // When : 신규 주문 생성
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItems(orderItems)
                .build();

        // When: 주문 취소
        order.cancel();
    }
}