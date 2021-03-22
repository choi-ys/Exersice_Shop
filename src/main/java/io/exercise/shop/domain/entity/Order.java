package io.exercise.shop.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/03/22 1:48 오후
 * @Content : 주문을 표현하는 Entity, order_tb 테이블과 매핑
 */
@Entity @Table(name = "order_tb")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_no")
    private Long orderNo;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    @OneToOne
    @JoinColumn(name = "delivery_no")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItemList;

    /** 연관관계 편의 메소드
     * 신규 주문 정보 생성 시, 주문의 회원 정보를 설정
     * @param member
     */
    public void setMember(Member member){
        this.member = member;
        member.getOrderList().add(this);
    }

    /** 연관관계 편의 메소드
     * 신규 주문 정보 생성 시, 주문의 품목 정보를 설정
     * @param orderItem
     */
    public void addOrderItemList(OrderItem orderItem){
        this.orderItemList.add(orderItem);
        orderItem.setOrder(this);
    }

    /** 연관관계 편의 메소드
     *  신규 주문 정보 생성 시, 주문의 배송 정보를 설정
     * @param delivery
     */
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}