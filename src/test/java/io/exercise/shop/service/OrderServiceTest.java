package io.exercise.shop.service;

import io.exercise.shop.domain.entity.DeliveryStatus;
import io.exercise.shop.domain.entity.Member;
import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.domain.entity.OrderStatus;
import io.exercise.shop.domain.entity.item.Item;
import io.exercise.shop.exception.NotEnoughStockException;
import io.exercise.shop.generator.DeliveryGenerator;
import io.exercise.shop.generator.ItemGenerator;
import io.exercise.shop.generator.MemberGenerator;
import io.exercise.shop.repository.ItemRepository;
import io.exercise.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author : choi-ys
 * @date : 2021/03/31 9:19 오전
 * @Content : 주문 관련 Service Test Case
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@Import({MemberGenerator.class, ItemGenerator.class, DeliveryGenerator.class})
@DisplayName("Service:OrderService")
class OrderServiceTest {

    @Resource MemberGenerator memberGenerator;
    @Autowired MemberRepository memberRepository;

    @Resource ItemGenerator itemGenerator;
    @Autowired ItemRepository itemRepository;

    @Autowired OrderService orderService;

    @Test
    @DisplayName("신규 주문 생성 및 조회")
//    @Rollback(value = false)
    public void saveOrder(){
        // Given : 회원 생성
        Member member = memberGenerator.buildMember();
        memberRepository.save(member);
        assertThat(member.getMemberNo()).isNotZero();

        // Given : 상품 생성
        Item item = itemGenerator.buildBook();
        itemRepository.save(item);
        assertThat(item.getItemNo()).isNotZero();

        // Given : 주문 수량
        int orderCount = 3;

        // Given : 주문 전 상품 재고 수량
        int beforeItemStockQuantity = item.getStockQuantity();

        // When : 주문 생성 여부 확인
        long createdOrderNo = orderService.saveOrder(member.getMemberNo(), item.getItemNo(), orderCount);
        Order createdOrder = orderService.findOrder(createdOrderNo);

        // Then : 주문 관련 양방향 연관관계 객체의 값 설정 여부 확인
        assertEquals(createdOrder.getMember(), member);
        assertEquals(createdOrder.getOrderItemList().get(0).getItem(), item);

        // Then : 주문 생성 시, 주문수량만큼 품목의 재고 감소 여부 확인
        assertEquals(createdOrder.getOrderItemList().get(0).getItem().getStockQuantity(), beforeItemStockQuantity - orderCount);
    }

    /**
     * @Rollback(value = false)
     * https://woowabros.github.io/experience/2019/01/29/exception-in-transaction.html
     */
    @Test
    @DisplayName("주문수량이 재고를 초과하는 주문")
    public void saveOrder_NotEnoughStockException(){
        // Given : 회원 생성
        Member member = memberGenerator.buildMember();
        memberRepository.save(member);
        assertThat(member.getMemberNo()).isNotZero();

        // Given : 상품 생성
        Item item = itemGenerator.buildBook();
        itemRepository.save(item);
        assertThat(item.getItemNo()).isNotZero();

        // Given : 주문 수량
        int orderCount = 101;

        // Given : 주문 전 상품 재고 수량
        int beforeItemStockQuantity = item.getStockQuantity();

        // When & Then
        NotEnoughStockException notEnoughStockException = assertThrows(NotEnoughStockException.class, () -> {
            orderService.saveOrder(member.getMemberNo(), item.getItemNo(), orderCount);
        });
        assertEquals(notEnoughStockException.getMessage(), "재고가 부족합니다.");
    }

    @Test
    @DisplayName("주문 완료")
//    @Rollback(value = false)
    public void orderComplete(){
        // Given
        Member member = memberGenerator.buildMember();
        memberRepository.save(member);
        assertThat(member.getMemberNo()).isNotZero();

        // Given : 상품 생성
        Item item = itemGenerator.buildBook();
        itemRepository.save(item);
        assertThat(item.getItemNo()).isNotZero();

        // Given : 주문 수량
        int orderCount = 3;

        // When
        long createdOrderNo = orderService.saveOrder(member.getMemberNo(), item.getItemNo(), orderCount);
        Order createdOrder = orderService.findOrder(createdOrderNo);
        assertEquals(createdOrder.getOrderStatus(), OrderStatus.ORDER);
        assertEquals(createdOrder.getDelivery().getDeliveryStatus(), DeliveryStatus.READY);

        orderService.completeOrder(createdOrderNo);

        // Then
        assertEquals(createdOrder.getDelivery().getDeliveryStatus(), DeliveryStatus.COMPLETE);
    }

    @Test
    @DisplayName("주문 취소")
//    @Rollback(value = false)
    public void orderCancel(){
        // Given
        Member member = memberGenerator.buildMember();
        memberRepository.save(member);
        assertThat(member.getMemberNo()).isNotZero();

        // Given : 상품 생성
        Item item = itemGenerator.buildBook();
        itemRepository.save(item);
        assertThat(item.getItemNo()).isNotZero();

        // 주문 전 상품 재고
        int beforeStockQuantity = item.getStockQuantity();

        // Given : 주문 수량
        int orderCount = 3;

        // When
        long createdOrderNo = orderService.saveOrder(member.getMemberNo(), item.getItemNo(), orderCount);
        Order createdOrder = orderService.findOrder(createdOrderNo);
        assertEquals(createdOrder.getOrderStatus(), OrderStatus.ORDER);
        assertEquals(createdOrder.getDelivery().getDeliveryStatus(), DeliveryStatus.READY);

        orderService.cancelOrder(createdOrderNo);

        // Then : 주문 상태 변경 여부 확인
        assertEquals(createdOrder.getOrderStatus(), OrderStatus.CANCEL);

        // Then : 주문 취소 시, 주문수량 만큼 상품의 재고 증가 여부 확인
        assertEquals(item.getStockQuantity(), beforeStockQuantity);
    }

    @Test
    @DisplayName("배송 완료 상태의 주문 취소")
    public void orderCancel_CompleteDeliveryStatusException(){
        // Given
        Member member = memberGenerator.buildMember();
        memberRepository.save(member);
        assertThat(member.getMemberNo()).isNotZero();

        // Given : 상품 생성
        Item item = itemGenerator.buildBook();
        itemRepository.save(item);
        assertThat(item.getItemNo()).isNotZero();

        // 주문 전 상품 재고
        int beforeStockQuantity = item.getStockQuantity();

        // Given : 주문 수량
        int orderCount = 3;

        // When
        long createdOrderNo = orderService.saveOrder(member.getMemberNo(), item.getItemNo(), orderCount);
        Order createdOrder = orderService.findOrder(createdOrderNo);
        assertEquals(createdOrder.getOrderStatus(), OrderStatus.ORDER);
        assertEquals(createdOrder.getDelivery().getDeliveryStatus(), DeliveryStatus.READY);

        orderService.completeOrder(createdOrderNo);

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> {
            orderService.cancelOrder(createdOrderNo);
        });

        // Then : 배송 완료건의 주문 취소 시, 예외 발생 여부 확인
        assertEquals(illegalStateException.getMessage(), "이미 배송완료된 상품은 취소가 불가능합니다.");

        // Then : 배송 완료건의 주문 관련 데이터 확인
        assertEquals(createdOrder.getOrderStatus(), OrderStatus.ORDER);
        assertEquals(createdOrder.getDelivery().getDeliveryStatus(), DeliveryStatus.COMPLETE);
        assertEquals(item.getStockQuantity(), beforeStockQuantity - orderCount);
    }
}