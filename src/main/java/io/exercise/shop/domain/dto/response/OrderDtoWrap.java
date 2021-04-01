package io.exercise.shop.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/04/01 7:26 오후
 * @Content : API 반환 객체
 */
@Getter @Setter
public class OrderDtoWrap {

    @JsonProperty(value = "orderList")
    List<OrderDto> orderDtoList;
}