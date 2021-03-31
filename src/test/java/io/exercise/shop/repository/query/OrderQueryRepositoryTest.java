package io.exercise.shop.repository.query;

import io.exercise.shop.domain.entity.Member;
import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.domain.entity.OrderStatus;
import io.exercise.shop.domain.entity.item.Item;
import io.exercise.shop.domain.entity.query.OrderCriteria;
import io.exercise.shop.generator.ItemGenerator;
import io.exercise.shop.generator.MemberGenerator;
import io.exercise.shop.repository.OrderRepository;
import io.exercise.shop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author : choi-ys
 * @date : 2021/03/31 3:11 오후
 * @Content : 조회 조건에 해당하는 주문 조회 Test Case
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DisplayName("Repository:OrderQueryRepository")
class OrderQueryRepositoryTest {

    @Autowired EntityManager entityManager;
    @Autowired OrderRepository orderRepository;
    @Autowired OrderQueryRepository orderQueryRepository;
    @Autowired OrderService orderService;

    /** GenericType saveAll */
    private <T> void saveAll(List<T> paramList){
        for (T param : paramList) {
            entityManager.persist(param);
        }
        entityManager.flush();
    }

    /**
     * 회원 생성 & 상품 생성
     * 주문 생성
     */
    @BeforeEach
    @DisplayName("Test 수행에 필요한 주문 정보 생성")
    public void setUp(){
        List<Member> memberList = new MemberGenerator().generateMemberList();
        this.saveAll(memberList);

        List<Item> itemList = new ItemGenerator().generateItemList();
        this.saveAll(itemList);

        for (int i = 0; i < memberList.size(); i++) {
            orderService.saveOrder(memberList.get(i).getMemberNo(), itemList.get(i).getItemNo(), (i+1));
        }
    }

    @Test
    @DisplayName("주문조회 : 조회조건 없음")
    public void findOrderByCriteria_NoParam(){
        // Given
        OrderCriteria orderCriteria = new OrderCriteria();

        // When : 생성 쿼리 -> SELECT o FROM Order o JOIN o.member m
        List<Order> orderList = orderQueryRepository.findOrderByCriteria(orderCriteria);

        // Then
        printMemberList(orderList);
        assertThat(orderList.size()).isNotZero();
    }

    @Test
    @DisplayName("주문조회 : 회원명 조건")
    public void findOrderByCriteria_ByMemberName(){
        // Given
        OrderCriteria orderCriteria = new OrderCriteria();
        orderCriteria.setMemberName("최용석");

        // When : 생성 쿼리 -> SELECT o FROM Order o JOIN o.member m WHERE m.memberName LIKE :memberName
        List<Order> orderList = orderQueryRepository.findOrderByCriteria(orderCriteria);

        // Then
        printMemberList(orderList);
        for (Order order : orderList) {
            assertEquals(StringUtils.hasText(order.getMember().getMemberName()), true);
        }
    }

    @Test
    @DisplayName("주문조회 : 주문상태 조건")
    public void findOrderByCriteria_ByOrderStatus(){
        // Given
        OrderCriteria orderCriteria = new OrderCriteria();
        orderCriteria.setOrderStatus(OrderStatus.ORDER);

        // When : 생성 쿼리 -> SELECT o FROM Order o JOIN o.member m WHERE o.orderStatus = :orderStatus
        List<Order> orderList = orderQueryRepository.findOrderByCriteria(orderCriteria);

        // Then
        printMemberList(orderList);
        for (Order order : orderList) {
            assertEquals(order.getOrderStatus(), OrderStatus.ORDER);
        }
    }

    @Test
    @DisplayName("주문조회 : 회원명과 주문상태 조건")
    public void findOrderByCriteria_AllParam(){
        // Given
        OrderCriteria orderCriteria = new OrderCriteria();
        orderCriteria.setMemberName("최용석");
        orderCriteria.setOrderStatus(OrderStatus.ORDER);

        // When : 생성 쿼리 -> SELECT o FROM Order o JOIN o.member m WHERE o.orderStatus = :orderStatus AND m.memberName LIKE :memberName
        List<Order> orderList = orderQueryRepository.findOrderByCriteria(orderCriteria);

        // Then
        printMemberList(orderList);
        for (Order order : orderList) {
            assertEquals(StringUtils.hasText(order.getMember().getMemberName()), true);
            assertEquals(order.getOrderStatus(), OrderStatus.ORDER);
        }
    }

    private void printMemberList(List<Order> orderList) {
        System.out.println("================================================================");
        for (Order order : orderList) {
            System.out.println("[주문번호 : " + order.getOrderNo()
                    + " | 주문자 : " + order.getMember().getMemberName()
                    + "] : [품목번호] : " + order.getOrderItemList().get(0).getOrderItemNo()
                    + " : [구매수량] : " + order.getOrderItemList().get(0).getOrderCount()
                    + " -> [상품번호] : " + order.getOrderItemList().get(0).getItem().getItemNo()
                    + " -> [상품명] : " + order.getOrderItemList().get(0).getItem().getItemName()
            );
        }
        System.out.println("================================================================");
    }
}