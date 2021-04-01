package io.exercise.shop.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

/**
 * @author : choi-ys
 * @date : 2021/03/22 1:49 오후
 * @Content : 배송지 정보를 표현하는 Entity, delivery_tb 테이블과 매핑
 */
@Entity @Table(name = "delivery_tb")
@SequenceGenerator(
        name = "SEQUENCE_DELIVERY_ENTITY_SEQ_GENERATOR"
        , sequenceName = "SEQUENCE_DELIVERY_ENTITY_SEQ"
        , initialValue = 1
        , allocationSize = 1
)
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id @GeneratedValue(generator = "SEQUENCE_DELIVERY_ENTITY_SEQ_GENERATOR")
    @Column(name = "delivery_no")
    private Long deliveryNo;

    /**
     * @JsonIgnore를 명시한 이유
     * 양방향 연관관계를 가진 Entity를 직접 반환하는 경우
     * 객체 직렬화 시 상호 참조로 인해 발생하는 무한 루프를 방지 하기 위해
     * 한쪽에 @JsonIgnore를 명시하여 직렬화 과정에서 발생하는 무한루프를 방지
     */
    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;


    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/03/30 6:03 오후
    // * --------------------------------------------------------------

    /**
     * 배송정보 생성
     * @apiNote 도메인 생성
     * @param address 배송 주소지 정보
     */
    @Builder
    public Delivery(Address address) {
        this.address = address;
        this.deliveryStatus = DeliveryStatus.READY;
    }


    // * --------------------------------------------------------------
    // * Header : 양방향 연관관계 객체의 값 설정
    // * @author : choi-ys
    // * @date : 2021/03/30 8:51 오후
    // * --------------------------------------------------------------

    /**
     * 배송지에 해당하는 주문 정보 설정
     * @apiNote 양방향 연관관계 객체의 값 설정
     * @param order 배송지의 주문정보
     */
    protected void mappingOrder(Order order){
        this.order = order;
    }

    
    // * --------------------------------------------------------------
    // * Header : 비즈니스 로직
    // * @author : choi-ys
    // * @date : 2021/03/31 2:20 오후
    // * --------------------------------------------------------------
    
    /**
     * 완료된 주문 배송정보의 배송상태 변경
     * @apiNote 비즈니스 로직
     */
    protected void completeDelivery(){
        this.deliveryStatus = DeliveryStatus.COMPLETE;
    }
}