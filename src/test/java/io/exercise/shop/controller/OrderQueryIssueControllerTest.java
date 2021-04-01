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
        entityManager.flush();
        entityManager.clear();
    }


    /**
     * API에서 Entity를 직접 반환하는 경우 발생하는 3가지 이슈
     *  - 이슈 1 : 반환하는 Entity가 단방향 연관관계를 가지는 경우
     *    - 응답 반환 시 객체를 직렬화 하는 Jackson 라이브러리가
     *      반환 Entity와 연관관계를 가지는 초기화 되지 않은 Entity의
     *      프록시 객체를 직렬화 하는 과정에서 오류가 발생
     *   - 해결 :
     *     1. 초기화 되지 않은 프록시 객체를 LazyLoading을 이용하여 초기화 하여 직렬화 대상에 포함.
     *     2. @JsonIgnore를 이용하여 초기화 되지 않은 프록시 객체를 직렬화 대상에서 제외.
     *
     *  - 이슈 2 : 반환하는 Entity가 양방향 연관관계를 가지는 경우
     *    - 응답 반환 시 객체를 반환하기 위해 직렬화 하는 과정에서
     *      양방향 연관관계를 가진 Entity의 상호 참조로 인해 무한 루프
     *      발생 으로 인한 StackOverFlow 발생
     *   - 해결 : @JsonIgnore를 이용하여 직렬화 시 한쪽의 연관관계를 끊어 무한루프를 제거
     *
     *   - 이슈 3 : Entity 수정으로 인해 API Spec이 변경 되는 경우
     *    - 서비스를 운영하면서 발생하는 여러 수정 사항들로 인하여 Entity가 수정될 경우
     *      API Spec이 변경되며, 해당 API 사용 하는 여러 Client측 에서 장애 발생
     *
     *  - 결론 : 조회된 Entity의 데이터를 이용하여 필요한 DataSet이 구성된 DTO로
     *  변환 후 반환함으로써 Entity를 직접 반환 시 발생하는 이슈를 사전에 방지
     * @throws Exception
     */
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