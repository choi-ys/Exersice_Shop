package io.exercise.shop.repository;

import io.exercise.shop.domain.entity.Delivery;
import io.exercise.shop.domain.entity.Member;
import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.domain.entity.OrderItem;
import io.exercise.shop.domain.entity.item.Item;
import io.exercise.shop.generator.DeliveryGenerator;
import io.exercise.shop.generator.ItemGenerator;
import io.exercise.shop.generator.MemberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author : choi-ys
 * @date : 2021/03/30 9:43 오후
 * @Content :
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DisplayName("Repository:OrderRepository")
@Import({MemberGenerator.class, DeliveryGenerator.class, ItemGenerator.class})
class OrderRepositoryTest {

    @Resource
    MemberGenerator memberGenerator;

    @Autowired
    MemberRepository memberRepository;

    @Resource
    DeliveryGenerator deliveryGenerator;

    @Resource
    ItemGenerator itemGenerator;

    @Autowired
    ItemRepository itemRepository;


    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("신규 주문 생성 및 조회")
//    @Rollback(value = false)
    public void save(){
        // Given : 회원 생성
        Member member = memberGenerator.buildMember();
        memberRepository.save(member);

        // Given : 배송지 정보 생성
        Delivery delivery = deliveryGenerator.buildDelivery();

        // Given : 상품 정보 생성
        Item item = itemGenerator.buildBook();
        itemRepository.save(item);
        int beforeStockCount = item.getStockQuantity(); // 주문전 상품 재고
        int orderCount = 3;

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(item.getItemPrice())
                .orderCount(orderCount)
                .build();
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        // When
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItemList(orderItemList)
                .build();

        orderRepository.save(order);

        // Then : 주문 생성 시, 주문 수량만큼 상품의 재고 감소 여부 확인
        assertThat(order.getOrderNo()).isNotZero();

        // Then : 주문 생성 시, CascadeType.ALL 적용 여부 확인
        assertThat(delivery.getDeliveryNo()).isNotZero();
        assertThat(orderItem.getOrderItemNo()).isNotZero();

        // Then : 주문 생성 시, 주문 수량에 따른 상품 재고 감소 여부 확인
        assertEquals(item.getStockQuantity() + orderCount, beforeStockCount);

        Order createdOrder = orderRepository.findByOrderNo(order.getOrderNo());
        assertEquals(createdOrder, order);
    }
}