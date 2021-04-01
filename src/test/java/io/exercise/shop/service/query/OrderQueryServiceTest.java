package io.exercise.shop.service.query;

import io.exercise.shop.domain.entity.Member;
import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.domain.entity.OrderStatus;
import io.exercise.shop.domain.entity.item.Item;
import io.exercise.shop.domain.entity.query.OrderCriteria;
import io.exercise.shop.generator.ItemGenerator;
import io.exercise.shop.generator.MemberGenerator;
import io.exercise.shop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author : choi-ys
 * @date : 2021/03/31 7:00 오후
 * @Content : 조회 조건별 주문 조회 처리 부 Test Case
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DisplayName("Service:OrderQueryService")
class OrderQueryServiceTest {

    @Autowired OrderQueryService orderQueryService;

    @Autowired
    OrderService orderService;

    @Autowired
    EntityManager entityManager;

    /** GenericType saveAll */
    private <T> void saveAll(List<T> paramList){
        for (T param : paramList) {
            entityManager.persist(param);
        }
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
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("주문조회 : 조회조건 없음")
    public void findOrderByCriteria_NoParam(){
        // Given
        OrderCriteria orderCriteria = new OrderCriteria();

        // When
        List<Order> orderList = orderQueryService.findOrderByCriteria(orderCriteria);

        // Then
        printMemberList(orderList);
        assertThat(orderList.size()).isNotZero();
    }

    @Test
    @DisplayName("주문조회 : 회원명 조건")
    public void findOrderByCriteria_ByMemberName(){
        // Given
        OrderCriteria orderCriteria = new OrderCriteria();
        String memberName = "최용석";
        orderCriteria.setMemberName(memberName);

        // When
        List<Order> orderList = orderQueryService.findOrderByCriteria(orderCriteria);

        // Then
        assertThat(orderList.size()).isNotZero();
        assertEquals(orderList.get(0).getMember().getMemberName(), memberName);
    }

    @Test
    @DisplayName("주문조회 : 주문상태 조건")
    public void findOrderByCriteria_ByOrderStatus(){
        // Given
        OrderCriteria orderCriteria = new OrderCriteria();
        orderCriteria.setOrderStatus(OrderStatus.ORDER);

        // When
        List<Order> orderList = orderQueryService.findOrderByCriteria(orderCriteria);

        // Then
        assertThat(orderList.size()).isNotZero();
        for (Order order : orderList) {
            assertEquals(order.getOrderStatus(), OrderStatus.ORDER);
        }
    }

    @Test
    @DisplayName("주문조회 : 회원명과 주문상태 조건")
    public void findOrderByCriteria_AllParam(){
        // Given
        OrderCriteria orderCriteria = new OrderCriteria();
        String memberName = "최용석";
        orderCriteria.setMemberName(memberName);
        orderCriteria.setOrderStatus(OrderStatus.ORDER);

        // When
        List<Order> orderList = orderQueryService.findOrderByCriteria(orderCriteria);

        // Then
        assertThat(orderList.size()).isNotZero();
        for (Order order : orderList) {
            assertEquals(order.getMember().getMemberName(), memberName);
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