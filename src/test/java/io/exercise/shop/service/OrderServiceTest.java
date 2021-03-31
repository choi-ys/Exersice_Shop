package io.exercise.shop.service;

import io.exercise.shop.domain.entity.Member;
import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.domain.entity.item.Item;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @Rollback(value = false)
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
}