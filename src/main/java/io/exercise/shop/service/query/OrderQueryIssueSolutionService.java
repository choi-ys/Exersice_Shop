package io.exercise.shop.service.query;

import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.repository.query.OrderQueryIssueSolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryIssueSolutionService {

    private final OrderQueryIssueSolutionRepository orderQueryIssueSolutionRepository;

    public List<Order> findAllOrder(){
        return orderQueryIssueSolutionRepository.findAll();
    }
}