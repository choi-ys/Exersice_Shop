package io.exercise.shop.repository;

import io.exercise.shop.domain.entity.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/03/30 3:51 오후
 * @Content : 상품 Entity와 상품 Table의 연동 처리 부
 */
@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager entityManager;

    /**
     * 신규 상품 생성 : persist
     * 기존 상품 수정 : merge
     * @param item
     */
    public void save(Item item){
        if(item.getItemNo() == null){
            // 신규 상품 생성
            entityManager.persist(item);
        }else{
            // 기존 상품 수정 : merge로 동작하므로 설정되지 않은 항목이 null로 갱신될 가능성이 있음.
            entityManager.merge(item);
        }
    }

    /**
     * 특정 상품 조회
     * @param itemNo
     * @return
     */
    public Item findByItemNo(long itemNo){
        return entityManager.find(Item.class, itemNo);
    }

    /**
     * 상품 목록 조회
     * @return
     */
    public List<Item> findAll(){
        return entityManager.createQuery("select i from Item as i", Item.class).getResultList();
    }

    /**
     * 상품 삭제
     * @param item
     */
    public void remove(Item item){
        entityManager.remove(item);
    }
}
