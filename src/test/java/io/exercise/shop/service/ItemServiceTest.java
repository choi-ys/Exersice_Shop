package io.exercise.shop.service;

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
import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author : choi-ys
 * @date : 2021/03/30 4:47 오후
 * @Content : 상품 Service Test Case
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DisplayName("Service:ItemService")
@Import(ItemGenerator.class)
class ItemServiceTest {

    @Resource
    ItemGenerator itemGenerator;

    @Autowired
    EntityManager entityManager;

    @Autowired
    ItemService itemService;

    @Test
    @DisplayName("상품 생성")
//    @Rollback(value = false)
    public void saveItem(){
        // Given
        Item newItem = itemGenerator.buildBook();

        // When
        itemService.saveItem(newItem);

        // Then
        assertThat(newItem.getItemNo()).isNotZero();
    }

    @Test
    @DisplayName("상품 조회")
//    @Rollback(value = false)
    public void findItem(){
        // Given
        Item newItem = itemGenerator.buildBook();
        itemService.saveItem(newItem);
        assertThat(newItem.getItemNo()).isNotZero();

        // When
        entityManager.flush();
        Item savedItem = itemService.findItem(newItem.getItemNo());

        // Then
        assertEquals(savedItem, newItem);
    }

    @Test
    @DisplayName("상품 수정")
//    @Rollback(value = false)
    public void mergeItem(){
        // Given
        Item newItem = itemGenerator.buildBook();
        itemService.saveItem(newItem);
        assertThat(newItem.getItemNo()).isNotZero();

        // When
        int updatePrice = 28500;
        int updateStockQuantity = 30;
        newItem.changeItemPrice(updatePrice);
        newItem.changeStockQuantity(updateStockQuantity);
        itemService.saveItem(newItem);

        // Then
        Item updatedItem = itemService.findItem(newItem.getItemNo());
        assertEquals(updatedItem, newItem);
        assertEquals(updatedItem.getItemPrice(), updatePrice);
        assertEquals(updatedItem.getStockQuantity(), updateStockQuantity);
    }

    @Test
    @DisplayName("상품 목록 조회")
    public void findItemList(){
        // Given
        Item firstItem = itemGenerator.buildBook();
        Item secondItem = itemGenerator.buildBook();
        Item thirdItem = itemGenerator.buildBook();
        itemService.saveItem(firstItem);
        itemService.saveItem(secondItem);
        itemService.saveItem(thirdItem);

        // When
        List<Item> itemList = itemService.findItemList();

        // Then
        assertThat(itemList.size()).isNotZero();
        assertThat(itemList.contains(firstItem));
        assertThat(itemList.contains(secondItem));
        assertThat(itemList.contains(thirdItem));
    }

    @Test
    @DisplayName("상품 삭제")
//    @Rollback(value = false)
    public void deleteItem(){
        // Given
        Item newItem = itemGenerator.buildBook();
        itemService.saveItem(newItem);
        assertThat(newItem.getItemNo()).isNotZero();

        // When
        itemService.deleteItem(newItem.getItemNo());

        // Then
        assertThat(itemService.findItem(newItem.getItemNo())).isNull();
    }
}