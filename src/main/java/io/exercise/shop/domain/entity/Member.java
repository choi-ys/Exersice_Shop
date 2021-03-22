package io.exercise.shop.domain.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author : choi-ys
 * @date : 2021/03/22 10:32 오전
 * @Content : member_tb 테이블과 Mapping되는 회원 Entity Class
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
}