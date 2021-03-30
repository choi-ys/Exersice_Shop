package io.exercise.shop.service;

import io.exercise.shop.domain.entity.item.Item;
import io.exercise.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/03/30 4:42 오후
 * @Content : 상품 관련 로직 처리 부
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    public Item findItem(long itemNo){
        return itemRepository.findByItemNo(itemNo);
    }

    public List<Item> findItemList(){
        return itemRepository.findAll();
    }

    @Transactional
    public void deleteItem(long itemno){
        Item item = this.findItem(itemno);
        itemRepository.remove(item);
    }
}