package io.exercise.shop.domain.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * @author : choi-ys
 * @date : 2021/03/22 2:13 오후
 * @Content : 주소를 표현 하는 값 타입
 */
@Embeddable @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String city;
    private String street;
    private String zipcode;


    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/03/30 9:17 오후
    // * --------------------------------------------------------------
    
    /**
     * 주소지 정보 생성
     * @apiNote 도메인 생성
     * @param city 행정구역 주소
     * @param street 도로명 주소
     * @param zipcode 우편번호
     */
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}