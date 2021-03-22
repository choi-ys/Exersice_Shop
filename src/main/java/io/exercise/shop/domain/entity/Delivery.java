package io.exercise.shop.domain.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

/**
 * @author : choi-ys
 * @date : 2021/03/22 1:49 오후
 * @Content : 배송지 정보를 표현하는 Entity, delivery_tb 테이블과 매핑
 */
@Entity @Table(name = "delivery_tb")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_no")
    private Long deliveryNo;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
}