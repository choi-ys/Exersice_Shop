package io.exercise.shop.controller;

import io.exercise.shop.domain.entity.Member;
import io.exercise.shop.domain.entity.item.Item;
import io.exercise.shop.generator.ItemGenerator;
import io.exercise.shop.generator.MemberGenerator;
import io.exercise.shop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@DisplayName("API:OrderQueryIssueSolution")
class OrderQueryIssueControllerTest {

    @Autowired OrderService orderService;
    @Autowired EntityManager entityManager;
    @Autowired MockMvc mockMvc;

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
    @DisplayName("V1:Entity 직접 반환으로 인한 이슈")
    public void getOrderListV1() throws Exception {
        // Given
        String urlTemplate = "/api/order/v1";

        // When
        ResultActions resultActions = this.mockMvc.perform(get(urlTemplate)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                ;
    }
}