package io.exercise.shop.repository;

import io.exercise.shop.domain.entity.item.Book;
import io.exercise.shop.domain.entity.item.Item;
import io.exercise.shop.generator.ItemGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


/**
 * @author : choi-ys
 * @date : 2021/03/30 3:55 오후
 * @Content : 상품 Repository Test Case
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DisplayName("Repository:ItemRepository")
@Import(ItemGenerator.class)
class ItemRepositoryTest {

    @Resource
    ItemGenerator itemGenerator;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장")
    @Rollback(value = false)
    public void save(){
        // Given
        String itemName = "자바 ORM 표준 JPA 프로그래밍";
        int itemPrice = 43000;
        int stockQuantity = 100;
        String author = "김영한";
        String isbn = "9788960777330";

        // When
        Book book = Book.builder()
                .itemName(itemName)
                .itemPrice(itemPrice)
                .stockQuantity(stockQuantity)
                .author(author)
                .isbn(isbn)
                .build();
        itemRepository.save(book);

        // Then
        assertThat(book.getItemNo()).isNotZero();
        assertEquals(book.getItemName(), itemName);
        assertEquals(book.getItemPrice(), itemPrice);
        assertEquals(book.getStockQuantity(), stockQuantity);
        assertEquals(book.getAuthor(), author);
        assertEquals(book.getIsbn(), isbn);
    }

    @Test
    @DisplayName("특정 상품 조회")
    public void findByItemNo(){
        // Given
        Item newItem = itemGenerator.BuildBook();
        itemRepository.save(newItem);
        assertThat(newItem.getItemNo()).isNotZero();

        // When
        Item savedItem = itemRepository.findByItemNo(newItem.getItemNo());

        // Then
        assertEquals(savedItem, newItem);
    }

    @Test
    @DisplayName("상품 수정")
    public void mergeItem(){
        // Given
        Item newItem = itemGenerator.createNewItem();
        itemRepository.save(newItem);
        assertThat(newItem.getItemNo()).isNotZero();

        // When
        int updatePrice = 22000;
        int updateStockQuantity = 55;
        newItem.setItemPrice(updatePrice);
        newItem.setStockQuantity(updateStockQuantity);

        itemRepository.save(newItem);

        // Then
        Item updatedItem = itemRepository.findByItemNo(newItem.getItemNo());
        assertEquals(updatedItem, newItem);
        assertEquals(updatedItem.getItemPrice(), updatePrice);
        assertEquals(updatedItem.getStockQuantity(), updateStockQuantity);
    }

    @Test
    @DisplayName("상품 목록 조회")
    public void findAll(){
        // Given
        Item firstItem = itemGenerator.BuildBook();
        Item secondItem = itemGenerator.BuildBook();
        Item thirdItem = itemGenerator.BuildBook();
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);
        itemRepository.save(thirdItem);

        assertThat(firstItem.getItemNo()).isNotZero();
        assertThat(secondItem.getItemNo()).isNotZero();
        assertThat(thirdItem.getItemNo()).isNotZero();
        
        // When
        List<Item> itemList = itemRepository.findAll();

        // Then
        assertThat(itemList.size()).isNotZero();
        assertThat(itemList.contains(firstItem));
        assertThat(itemList.contains(secondItem));
        assertThat(itemList.contains(thirdItem));
    }

    @Test
    @DisplayName("상품 삭제")
    @Rollback(value = false)
    public void remove(){
        // Given
        Item newItem = itemGenerator.BuildBook();
        itemRepository.save(newItem);
        assertThat(newItem.getItemNo()).isNotZero();

        // When
        itemRepository.remove(newItem);

        // Then
        assertThat(this.itemRepository.findByItemNo(newItem.getItemNo())).isNull();
    }
}