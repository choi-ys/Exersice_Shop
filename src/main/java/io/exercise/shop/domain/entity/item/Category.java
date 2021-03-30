package io.exercise.shop.domain.entity.item;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

/**
 * @author : choi-ys
 * @date : 2021/03/22 2:09 오후
 * @Content : 카테고리를 표현하는 Entity, category_tb 테이블과 매핑
 */
@Entity @Table(name = "category_tb")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_no")
    private Long categoryNo;

    private String categoryName;

    @ManyToMany(fetch = LAZY)
    @JoinTable(name = "category_item_tb",
            joinColumns = @JoinColumn(name = "category_no"),
            inverseJoinColumns = @JoinColumn(name = "item_no")
    )
    private List<Item> itemList = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_no")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> childCategory;


    // * --------------------------------------------------------------
    // * Header : 양방향 연관관계 객체의 값 설정
    // * @author : choi-ys
    // * @date : 2021/03/30 9:07 오후
    // * --------------------------------------------------------------

    /**
     * 자식 카테고리 추가 시, 부모 카테고리 객체와 자식 카테고리 객체 양쪽에 값 설정
     * @apiNote 양방향 연관관계 객체의 값 설정
     * @param childCategory 자식 카테고리
     */
    public void addChildCategory(Category childCategory){
        this.childCategory.add(childCategory);
        childCategory.setParentCategory(this);
    }

    /**
     * 부모 카테고리 설정
     * @apiNote 양방향 연관관계 객체의 값 설정
     * @param category 부모 카테고리
     */
    private void setParentCategory(Category category){
        this.parentCategory = category;
    }
}