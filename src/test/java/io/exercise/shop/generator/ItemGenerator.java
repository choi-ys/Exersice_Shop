package io.exercise.shop.generator;

import io.exercise.shop.domain.entity.item.Book;
import io.exercise.shop.domain.entity.item.Item;

/**
 * @author : choi-ys
 * @date : 2021/03/23 3:57 오후
 * @Content : 상품 관련 Test에 필요한 Item Entity 생성
 */
public class ItemGenerator {

    public Item createNewItem(){
        return makeBookEntity();
    }

    private Item makeBookEntity(){
        String itemName = "자바 ORM 표준 JPA 프로그래밍";
        int itemPrice = 43000;
        int stockCount = 100;
        String author = "김영한";
        String isbn = "9788960777330";

        Book book = new Book();
        book.setItemName(itemName);
        book.setItemPrice(itemPrice);
        book.setStockQuantity(stockCount);
        book.setAuthor(author);
        book.setIsbn(isbn);
        return book;
    }
}