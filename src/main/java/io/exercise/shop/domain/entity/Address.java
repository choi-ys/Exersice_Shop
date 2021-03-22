package io.exercise.shop.domain.entity;

import javax.persistence.Embeddable;

/**
 * @author : choi-ys
 * @date : 2021/03/22 2:13 오후
 * @Content : 주소를 표현 하는 값 타입
 */
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address(){
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}