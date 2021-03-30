package io.exercise.shop.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

/**
 * @author : choi-ys
 * @date : 2021/03/22 10:32 오전
 * @Content : 회원을 표현하는 Entity, member_tb 테이블과 매핑
 */
@Entity @Table(name = "member_tb")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_no")
    private Long memberNo;

    @Column(name = "member_name")
    private String memberName;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<Order> orderList = new ArrayList<>();


    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/03/30 6:36 오후
    // * --------------------------------------------------------------

    /**
     * 회원 정보 생성
     * @apiNote 도메인 생성
     * @param memberName 회원 이름
     * @param address 회원 주소지 정보
     * @return 신규 회원 정보
     */
    @Builder
    public Member(String memberName, Address address) {
        this.memberName = memberName;
        this.address = address;
    }
    
    // * --------------------------------------------------------------
    // * Header : 비즈니스 로직
    // * @author : choi-ys
    // * @date : 2021/03/30 7:57 오후
    // * --------------------------------------------------------------

    /**
     * 회원 주소 변경
     * @apiNote 비즈니스 로직
     * @param address 변경 주소지 정보
     */
    public void changeAddress(Address address){
        this.address = address;
    }
}