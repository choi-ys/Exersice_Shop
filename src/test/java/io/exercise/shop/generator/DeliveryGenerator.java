package io.exercise.shop.generator;

import io.exercise.shop.domain.entity.Address;
import io.exercise.shop.domain.entity.Delivery;

/**
 * @author : choi-ys
 * @date : 2021/03/30 8:14 오후
 * @Content :
 */
public class DeliveryGenerator {

    public Delivery buildDelivery(){
        Address address = new Address("서울특별시", "강남구 테헤란로 325", "06151");
        return Delivery.builder()
                .address(address)
                .build();
    }
}
