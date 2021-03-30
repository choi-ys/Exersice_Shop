package io.exercise.shop.domain.entity.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author : choi-ys
 * @date : 2021/03/22 2:02 오후
 * @Content : 상품의 공통 항목을 표현하는 Item Entity를 상속받아 영화 상품을 표현하는 Entity
 */
@Entity @DiscriminatorValue("M")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends Item{

    private String director;
    private String actor;
}