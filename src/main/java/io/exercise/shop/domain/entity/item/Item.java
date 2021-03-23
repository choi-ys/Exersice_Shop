package io.exercise.shop.domain.entity.item;

import io.exercise.shop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

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
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_no")
    private Long No;

    @ManyToMany(mappedBy = "itemList", fetch = FetchType.LAZY)
    private List<Category> categoryList = new ArrayList<>();

    private String itemName;

    private int itemPrice;

    private int stockQuantity;

    // stock(재고) 증가
    public void addStockCount(int quantity){
        this.stockQuantity += quantity;
    }

    // stock(재고) 감소
    public void removeStockCount(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("재고가 부족합니다.");
        }
        this.stockQuantity = restStock;
    }
}