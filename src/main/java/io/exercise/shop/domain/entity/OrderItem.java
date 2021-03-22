package io.exercise.shop.domain.entity;

import io.exercise.shop.domain.entity.item.Item;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

/**
 * @author : choi-ys
 * @date : 2021/03/22 1:56 오후
 * @Content : 주문과 상품의 N:M 관계를 해결하기 위해 품목 정보를 표현하는 연결 Entity
 *  - 주문과 상품이 결합되었을때 의미 있는 정보를 표현하는 Entity
 *  - order_item_tb 테이블과 매핑
 */
@Entity @Table(name = "order_item_tb")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_no")
    private Long orderItemNo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_no")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_no")
    private Item item;

    private long orderPrice;

    private int orderCount;
}