package io.exercise.shop.domain.entity.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author : choi-ys
 * @date : 2021/03/22 2:02 오후
 * @Content : 상품의 공통 항목을 표현하는 Item Entity를 상속받아 음반 상품을 표현하는 Entity
 */
@Entity @DiscriminatorValue("A")
@Getter @Setter
public class Album extends Item{

    private String artist;
    private String etc;
}