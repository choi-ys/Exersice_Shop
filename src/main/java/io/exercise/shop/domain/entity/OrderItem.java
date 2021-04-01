package io.exercise.shop.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@SequenceGenerator(
        name = "SEQUENCE_ORDER_ITEM_ENTITY_SEQ_GENERATOR"
        , sequenceName = "SEQUENCE_ORDER_ITEM_ENTITY_SEQ"
        , initialValue = 1
        , allocationSize = 1
)
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue(generator = "SEQUENCE_ORDER_ITEM_ENTITY_SEQ_GENERATOR")
    @Column(name = "order_item_no")
    private Long orderItemNo;

    /**
     * @JsonIgnore를 명시한 이유
     * 양방향 연관관계를 가진 Entity를 직접 반환하는 경우
     * 객체 직렬화 시 상호 참조로 인해 발생하는 무한 루프를 방지 하기 위해
     * 한쪽에 @JsonIgnore를 명시하여 직렬화 과정에서 발생하는 무한루프를 방지
     */
    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_no")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_no")
    private Item item;

    private long orderPrice;

    private int orderCount;


    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/03/30 5:49 오후
    // * --------------------------------------------------------------

    /**
     * 품목 생성
     * @apiNote 도메인 생성
     * @param item 주문 대상 상품
     * @param orderPrice 주문 상품 금액
     * @param orderCount 주문 수량
     */
    @Builder
    public OrderItem(Item item, long orderPrice, int orderCount) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.orderCount = orderCount;
        item.removeStockCount(orderCount);
    }


    // * --------------------------------------------------------------
    // * Header : 비즈니스 로직
    // * @author : choi-ys
    // * @date : 2021/03/30 5:25 오후
    // * --------------------------------------------------------------

    /**
     * 주문 취소 시, 주문 수량만큼 주문 상품의 재고 원복
     * @apiNote 비즈니스 로직
     */
    public void cancel() {
        item.addStockCount(orderCount);
    }

    /**
     * 품목의 총액
     * @apiNote 비즈니스 로직
     * @return 해당 품목 가격*수량
     */
    public long getTotalOrderItemPrice() {
        return orderPrice * orderCount;
    }

    
    // * --------------------------------------------------------------
    // * Header : 양방향 연관관계 객체의 값 설정
    // * Header : 도메인 생성 로직
    // * Header : 비즈니스 로직
    // * @author : choi-ys
    // * @date : 2021/03/30 8:51 오후
    // * --------------------------------------------------------------

    /**
     * 품목에 해당하는 주문 정보 설정
     * @apiNote 양방향 연관관계 객체의 값 설정
     * @param order 품목의 주문정보
     */
    protected void mappingOrder(Order order){
        this.order = order;
    }
}