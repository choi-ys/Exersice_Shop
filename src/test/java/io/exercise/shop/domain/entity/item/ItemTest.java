package io.exercise.shop.domain.entity.item;

import io.exercise.shop.exception.NotEnoughStockException;
import io.exercise.shop.generator.ItemGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author : choi-ys
 * @date : 2021/03/23 2:01 오후
 * @Content : Item Entity의 필드에 접근하는 로직 Test
 */
@DisplayName("Entity:Item")
class ItemTest {

    @Test
    @DisplayName("상품 생성")
    public void createBook(){
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

        // Then
        assertEquals(book.getItemName(), itemName);
        assertEquals(book.getItemPrice(), itemPrice);
        assertEquals(book.getStockQuantity(), stockQuantity);
        assertEquals(book.getAuthor(), author);
        assertEquals(book.getIsbn(), isbn);
    }

    @Test
    @DisplayName("재고 증가")
    public void addStockCount(){
        // Given
        Item item = new ItemGenerator().BuildBook();
        int initialStockCount = item.getStockQuantity();
        int orderQuantity = 5;

        // When
        item.addStockCount(orderQuantity);

        // Then
        assertEquals(item.getStockQuantity(), (initialStockCount + orderQuantity));
    }

    @Test
    @DisplayName("재고 감소")
    public void removeStockCount_NotEnoughStockException(){
        // Given
        Item item = new ItemGenerator().BuildBook();
        int initialStockCount = item.getStockQuantity();
        int orderQuantity = 5;

        // When
        item.removeStockCount(orderQuantity);

        // Then
        assertEquals(item.getStockQuantity(), (initialStockCount-orderQuantity));
    }

    @Test
    @DisplayName("재고 부족 에외처리")
    public void removeStockCount(){
        // Given
        Item item = new ItemGenerator().BuildBook();
        int orderQuantity = 101;

        // When
        NotEnoughStockException notEnoughStockException = assertThrows(NotEnoughStockException.class, () -> {
            item.removeStockCount(orderQuantity);
        });

        // Then
        assertThat(notEnoughStockException.getMessage()).isEqualTo("재고가 부족합니다.");
    }
}