package io.exercise.shop.domain.entity.item;

import io.exercise.shop.exception.NotEnoughStockException;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/03/22 2:01 오후
 * @Content : 상품의 공통 항목을 표현하는 부모 Entity, item_tb 테이블과 매핑
 */
@Entity @Table(name = "item_tb")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) @DiscriminatorColumn(name = "d_type")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_no")
    private Long itemNo;

    @ManyToMany(mappedBy = "itemList", fetch = FetchType.LAZY)
    private List<Category> categoryList = new ArrayList<>();

    private String itemName;

    private int itemPrice;

    private int stockQuantity;

    
    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/03/30 2:20 오후
    // * --------------------------------------------------------------

    /**
     * 신규 상품 생성
     * @apiNote 도메인 생성
     * @param itemName 상품명
     * @param itemPrice 상품가격
     * @param stockQuantity 상품재고 수량
     */
    protected Item(String itemName, int itemPrice, int stockQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.stockQuantity = stockQuantity;
    }


    // * --------------------------------------------------------------
    // * Header : 비즈니스 로직
    // * @author : choi-ys
    // * @date : 2021/03/30 2:20 오후
    // * --------------------------------------------------------------
    
    /**
     * 재고 증가 처리
     * @apiNote 비즈니스 로직
     * @param quantity 증가할 재고 수량
     */
    public void addStockCount(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소 처리
     * @apiNote 비즈니스 로직
     * @param quantity 감소할 재고 수량
     * @exception NotEnoughStockException 재고 부족 오류
     */
    public void removeStockCount(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("재고가 부족합니다.");
        }
        this.stockQuantity = restStock;
    }
}