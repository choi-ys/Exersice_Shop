package io.exercise.shop.service;

import io.exercise.shop.domain.entity.Delivery;
import io.exercise.shop.domain.entity.Member;
import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.domain.entity.OrderItem;
import io.exercise.shop.domain.entity.item.Item;
import io.exercise.shop.repository.ItemRepository;
import io.exercise.shop.repository.MemberRepository;
import io.exercise.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/03/31 9:10 오전
 * @Content : 주문 관련 로직 처리 부
 *  - 신규 주문
 *  - 주문 취소
 *  - 주문 조회
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    /**
     * 신규 주문
     * @param memberNo 주문 회원 번호
     * @param itemNo 주문 상품 번호
     * @param orderCount 주문 수량
     * @return 생성된 주문 번호
     */
    @Transactional
    public long saveOrder(long memberNo, long itemNo, int orderCount){
        // 주문 회원 정보
        Member member = memberRepository.findByMemberNo(memberNo);

        // 상품 정보
        Item item = itemRepository.findByItemNo(itemNo);

        // 배송 정보 생성
        Delivery delivery = Delivery.builder()
                .address(member.getAddress())
                .build();

        // 품목 정보
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(item.getItemPrice())
                .orderCount(orderCount)
                .build();
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItemList(orderItemList)
                .build();
        orderRepository.save(order);
        return order.getOrderNo();
    }

    /**
     * 주문 조회
     * @param orderNo 조회 주문 번호
     * @return 조회 주문 정보
     */
    public Order findOrder(long orderNo){
        return orderRepository.findByOrderNo(orderNo);
    }

    /**
     * 주문 완료
     * @param orderNo
     */
    public void completeOrder(long orderNo){
        Order order = orderRepository.findByOrderNo(orderNo);
        order.completeOrder();
    }

    /**
     * 주문 취소
     * @param orderNo
     */
    public void cancelOrder(long orderNo){
        Order order = orderRepository.findByOrderNo(orderNo);
        order.cancel();
    }
}