package io.exercise.shop.service.query;

import io.exercise.shop.domain.dto.response.OrderDto;
import io.exercise.shop.domain.dto.response.OrderDtoWrap;
import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.repository.query.OrderQueryIssueSolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryIssueSolutionService {

    private final OrderQueryIssueSolutionRepository orderQueryIssueSolutionRepository;

    public List<Order> findAllOrder(){
        return orderQueryIssueSolutionRepository.findAll();
    }

    /**
     * OSIV OFF시 트랜잭션은 Service 계층까지만 존재하므로
     * 해당 계층에서 1:N 관계의 Entity를 LazyLoading하여 응답 객체로 변환
     * @return
     */
    public OrderDtoWrap findAllOrderAndMemberAndDelivery(){

        List<Order> orderList = orderQueryIssueSolutionRepository.findByFetchJoin();

        List<OrderDto> orderDtoList = orderList.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        OrderDtoWrap orderDtoWrap = new OrderDtoWrap();
        orderDtoWrap.setOrderDtoList(orderDtoList);

        return orderDtoWrap;
    }
}