package io.exercise.shop.domain.entity.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author : choi-ys
 * @date : 2021/03/22 2:02 오후
 * @Content : 상품의 공통 항목을 표현하는 Item Entity를 상속받아 도서 상품을 표현하는 Entity
 */
@Entity @DiscriminatorValue("B")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item{

    private String author;
    private String isbn;


    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/03/30 8:03 오후
    // * --------------------------------------------------------------

    /**
     * 도서 상품 생성
     * @apiNote 도메인 생성
     * @param itemName 상품명
     * @param itemPrice 상품가격
     * @param stockQuantity 상품재고 수량
     * @param author 저자
     * @param isbn 도서번호
     */
    @Builder
    public Book(String itemName, int itemPrice, int stockQuantity, String author, String isbn) {
        super(itemName, itemPrice, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }
}