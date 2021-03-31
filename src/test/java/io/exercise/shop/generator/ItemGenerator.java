package io.exercise.shop.generator;

import io.exercise.shop.domain.entity.item.Book;
import io.exercise.shop.domain.entity.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/03/23 3:57 오후
 * @Content : 상품 관련 Test에 필요한 Item Entity 생성
 */
public class ItemGenerator {
    String itemName = "자바 ORM 표준 JPA 프로그래밍";
    int itemPrice = 43000;
    int stockQuantity = 100;
    String author = "김영한";
    String isbn = "9788960777330";

    public Item buildBook(){
        return getItem(itemName);
    }

    public Item buildBookByItemName(String itemName){
        return getItem(itemName);
    }

    private Item getItem(String itemName) {
        return Book.builder()
                .itemName(itemName)
                .itemPrice(itemPrice)
                .stockQuantity(stockQuantity)
                .author(author)
                .isbn(isbn)
                .build();
    }

    public List<Item> generateItemList(){
        List<Item> itemList = new ArrayList<>();
        itemList.add(new ItemGenerator().buildBookByItemName("Head First Java"));
        itemList.add(new ItemGenerator().buildBookByItemName("Modern Java In Action"));
        itemList.add(new ItemGenerator().buildBookByItemName("Java 8 In Action"));
        itemList.add(new ItemGenerator().buildBookByItemName("Effective Java"));
        itemList.add(new ItemGenerator().buildBookByItemName("Clean Code"));
        itemList.add(new ItemGenerator().buildBookByItemName("Clean Architecture"));
        itemList.add(new ItemGenerator().buildBookByItemName("Building Microservices"));
        itemList.add(new ItemGenerator().buildBookByItemName("클라우드 네이티브 자바"));
        itemList.add(new ItemGenerator().buildBookByItemName("도메인 주도 설계 핵심"));
        itemList.add(new ItemGenerator().buildBookByItemName("토비의 스프링"));
        itemList.add(new ItemGenerator().buildBookByItemName("자바 ORM 표준 JPA 프로그래밍"));
        itemList.add(new ItemGenerator().buildBookByItemName("객체지향의 사실과 오해"));
        itemList.add(new ItemGenerator().buildBookByItemName("테스트 주도 개발"));
        return itemList;
    }
}