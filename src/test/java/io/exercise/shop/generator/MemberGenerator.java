package io.exercise.shop.generator;

import io.exercise.shop.domain.entity.Address;
import io.exercise.shop.domain.entity.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/03/23 9:35 오전
 * @Content : 회원 관련 Test에 필요한 Member Entity 생성
 */
public class MemberGenerator {
    
    public Member buildMember(){
        String memberName = "최용석";
        return getMember(memberName);
    }

    public Member buildMemberByMemberName(String memberName){
        return getMember(memberName);
    }

    private Member getMember(String memberName) {
        Address address = new Address("서울특별시", "강남구 테헤란로 325", "06151");
        return Member.builder()
                .memberName(memberName)
                .address(address)
                .build();
    }

    public List<Member> generateMemberList(){
        List<Member> memberList = new ArrayList<>();
        memberList.add(new MemberGenerator().buildMemberByMemberName("최용석"));
        memberList.add(new MemberGenerator().buildMemberByMemberName("이성욱"));
        memberList.add(new MemberGenerator().buildMemberByMemberName("박재현"));
        memberList.add(new MemberGenerator().buildMemberByMemberName("권성준"));
        memberList.add(new MemberGenerator().buildMemberByMemberName("이하은"));
        memberList.add(new MemberGenerator().buildMemberByMemberName("기호창"));
        memberList.add(new MemberGenerator().buildMemberByMemberName("전성원"));
        return memberList;
    }
}