package io.exercise.shop.service.query;

import io.exercise.shop.domain.entity.Member;
import io.exercise.shop.domain.entity.Order;
import io.exercise.shop.domain.entity.item.Item;
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

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Service:OrderQueryIssueService")
@Transactional
class OrderQueryIssueSolutionServiceTest {

    @Autowired
    OrderQueryIssueSolutionService orderQueryIssueSolutionService;

    @Autowired
    OrderService orderService;

    @Autowired
    EntityManager entityManager;

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
    @DisplayName("newTest")
    public void newTest() throws Exception {
        // Given

        // When
        List<Order> allOrder = orderQueryIssueSolutionService.findAllOrder();

        // Then
    }
}