package io.exercise.shop.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

/**
 * @author : choi-ys
 * @date : 2021/03/22 10:32 오전
 * @Content : 회원을 표현하는 Entity, member_tb 테이블과 매핑
 */
@Entity @Table(name = "member_tb")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_no")
    private Long memberNo;

    @Column(name = "member_name")
    private String memberName;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<Order> orderList;
}