package io.exercise.shop.domain.entity;

import io.exercise.shop.generator.DeliveryGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author : choi-ys
 * @date : 2021/03/30 6:14 오후
 * @Content : 배송 Entiy Test
 */
class DeliveryTest {

    @Test
    @DisplayName("배송 정보 생성")
    public void createDelivery(){
        // Given
        Address address = new Address("서울 특별시 강남구", "테헤란로 325", "06141");

        // When
        Delivery delivery = new DeliveryGenerator().buildDelivery();

        // Then
        assertEquals(delivery.getDeliveryStatus(), DeliveryStatus.READY);
    }
}