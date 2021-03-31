package io.exercise.shop.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/03/22 1:48 오후
 * @Content : 주문을 표현하는 Entity, order_tb 테이블과 매핑
 */
@Entity @Table(name = "order_tb")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_no")
    private Long orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_no")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList = new ArrayList<>();


    // * --------------------------------------------------------------
    // * Header : 양방향 연관관계 객체의 값 설정
    // * @author : choi-ys
    // * @date : 2021/03/30 5:15 오후
    // * --------------------------------------------------------------

    /**
     * 신규 주문 정보 생성 시, 주문의 회원 정보를 설정
     * @apiNote 양방향 연관관계 객체의 값 설정
     * @param member 주문 회원 정보
     */
    public void setMember(Member member){
        this.member = member;
        member.getOrderList().add(this);
    }

    /**
     * 신규 주문 정보 생성 시, 주문의 품목 정보를 설정
     * @apiNote 양방향 연관관계 객체의 값 설정
     * @param orderItem 주문 품목 정보
     */
    public void addOrderItemList(OrderItem orderItem){
        this.orderItemList.add(orderItem);
        orderItem.mappingOrder(this);
    }

    /**
     * 신규 주문 정보 생성 시, 주문의 배송 정보를 설정
     * @apiNote 양방향 연관관계 객체의 값 설정
     * @param delivery 주문 배송지 정보
     */
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.mappingOrder(this);
    }


    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/03/30 5:15 오후
    // * --------------------------------------------------------------

    /**
     * 신규 주문 생성
     * @apiNote 도메인 생성
     * @param member 주문 회원 정보
     * @param delivery 배송 정보
     * @param orderItemList 주문 상품 목록 정보
     * @return 신규 주문 정보
     */
    @Builder
    public Order(Member member, Delivery delivery, List<OrderItem> orderItemList) {
        this.setMember(member);
        this.setDelivery(delivery);
        this.orderStatus = OrderStatus.ORDER;
        this.orderDate = LocalDateTime.now();
        for (OrderItem orderItem : orderItemList) {
            this.addOrderItemList(orderItem);
        }
    }

    
    // * --------------------------------------------------------------
    // * Header : 비즈니스 로직
    // * @author : choi-ys
    // * @date : 2021/03/30 5:20 오후
    // * --------------------------------------------------------------

    /**
     * 주문 취소
     *  - 해당 주문의 상태를 취소로 변경
     *  - 해당 주문의 주문 수량만큼 각 주문 상품의 재고 원복
     * @apiNote 비즈니스 로직
     * @throws IllegalStateException 해당 주문의 배송 상태가 'COMPLETE(배송완료)'인 경우 취소 불가 예외 처리
     */
    public void cancel() {
        if(delivery.getDeliveryStatus() == DeliveryStatus.COMPLETE){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItemList) {
            orderItem.cancel();
        }
    }

    /**
     * 주문 총액 조회
     * @apiNote 비즈니스 로직
     * @return 주문 총액
     */
    public int getTotalOrderPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItemList) {
            totalPrice += orderItem.getTotalOrderItemPrice();
        }
        return totalPrice;
    }
}