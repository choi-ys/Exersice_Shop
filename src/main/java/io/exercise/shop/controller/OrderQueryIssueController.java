package io.exercise.shop.controller;

import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.service.query.OrderQueryIssueSolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(
        value = "/api/order",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class OrderQueryIssueController {

    private final OrderQueryIssueSolutionService orderQueryIssueSolutionService;

    /**
     * Entity를 그대로 반환하는 API
     * @return
     */
    @GetMapping(value = "/v1")
    public List<Order> getOrderListV1(){
        List<Order> allOrder = orderQueryIssueSolutionService.findAllOrder();
        for (Order order : allOrder) {
            order.getMember().getMemberName();
            order.getDelivery().getDeliveryStatus();
        }
        return allOrder;
    }
}