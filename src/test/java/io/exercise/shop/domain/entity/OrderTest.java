package io.exercise.shop.domain.entity;

import io.exercise.shop.domain.entity.item.Item;
import io.exercise.shop.generator.DeliveryGenerator;
import io.exercise.shop.generator.ItemGenerator;
import io.exercise.shop.generator.MemberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        // When : 신규 주문 생성
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItemList(orderItemList)
                .build();

        // Then : 양방향 연관관계를 가진 객체의 값 동기화 여부 확인
        assertThat(member.getOrderList().contains(order));
        assertEquals(delivery.getOrder(), order);
        assertEquals(orderItem.getOrder(), order);

        // Then : 주문 데이터 정상 여부 확인
        assertEquals(order.getOrderStatus(), OrderStatus.ORDER);
        assertThat(order.getOrderDate()).isNotNull();

        // Then : 주문 생성 시, 주문 수량만큼 상품의 재고 감소 여부 확인
        assertEquals(item.getStockQuantity() + orderCount, beforeStockCount);
    }

    @Test
    @DisplayName("주문 완료")
    public void newTest(){
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
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        // When : 신규 주문 생성
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItemList(orderItemList)
                .build();

        order.completeOrder();

        // Then : 양방향 연관관계를 가진 객체의 값 동기화 여부 확인
        assertThat(member.getOrderList().contains(order));
        assertEquals(delivery.getOrder(), order);
        assertEquals(orderItem.getOrder(), order);

        // Then : 주문 데이터 정상 여부 확인
        assertEquals(order.getOrderStatus(), OrderStatus.ORDER);
        assertThat(order.getOrderDate()).isNotNull();

        // Then : 주문 생성 시, 주문 수량만큼 상품의 재고 감소 여부 확인
        assertEquals(item.getStockQuantity() + orderCount, beforeStockCount);

        // Then : 주문 완료 시 배송 상태 완료 변경 여부 확인
        assertEquals(order.getDelivery().getDeliveryStatus(), DeliveryStatus.COMPLETE);
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
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        // When : 신규 주문 생성
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItemList(orderItemList)
                .build();

        // When: 주문 취소
        order.cancel();

        // Then : 양방향 연관관계를 가진 객체의 값 동기화 여부 확인
        assertThat(member.getOrderList().contains(order));
        assertEquals(delivery.getOrder(), order);
        assertEquals(orderItem.getOrder(), order);

        // Then : 주문 취소에 따른 데이터 정상 여부 확인
        assertEquals(order.getOrderStatus(), OrderStatus.CANCEL);
        assertThat(order.getOrderDate()).isNotNull();

        // Then : 주문 수량만큼 주문 상품의 재고 추가 여부 확인
        assertEquals(item.getStockQuantity(), beforeStockCount);

        // Then : 주문 완료 이전 취소 시 배송 상태 정상 여부 확인
        assertEquals(order.getDelivery().getDeliveryStatus(), DeliveryStatus.READY);
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
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        // When : 신규 주문 생성
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItemList(orderItemList)
                .build();

        assertEquals(order.getOrderStatus(), OrderStatus.ORDER);
        assertEquals(order.getDelivery().getDeliveryStatus(), DeliveryStatus.READY);

        order.completeOrder();
        assertEquals(order.getDelivery().getDeliveryStatus(), DeliveryStatus.COMPLETE);

        // When: 주문 취소
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> {
            order.cancel();
        });
        assertEquals(illegalStateException.getMessage(), "이미 배송완료된 상품은 취소가 불가능합니다.");

        // Then : 양방향 연관관계를 가진 객체의 값 동기화 여부 확인
        assertThat(member.getOrderList().contains(order));
        assertEquals(delivery.getOrder(), order);
        assertEquals(orderItem.getOrder(), order);

        // Then : 주문 데이터 정상 여부 확인
        assertEquals(order.getOrderStatus(), OrderStatus.ORDER);
        assertThat(order.getOrderDate()).isNotNull();

        // 배송 완료된 주문 상품 추쇠 요청 시, 주문 데이터 영향 여부 확인
        assertEquals(order.getDelivery().getDeliveryStatus(), DeliveryStatus.COMPLETE);
        assertEquals(item.getStockQuantity() + orderCount, beforeStockCount);
    }
}