package com.example.shoppingmall_restapi.factory;


import com.example.shoppingmall_restapi.entity.member.Authority;
import com.example.shoppingmall_restapi.entity.member.Member;

public class MemberFactory {

    public static Member createMember() {
        Member member = Member.builder()
                .username("user")
                .password("user123!")
                .email("user@test.com")
                .authority(Authority.ROLE_USER)
                .name("유저")
                .nickname("닉네임")
                .phone("010-1111-1111")
                .address("경기도~")
                .build();

        return member;
    }

    public static Member createSeller() {
        Member member = Member.builder()
                .username("guide")
                .password("user123!")
                .email("user2@test.com")
                .authority(Authority.ROLE_SELLER)
                .name("가이드")
                .nickname("닉네임")
                .phone("010-1111-1111")
                .address("경기도~")
                .build();

        return member;
    }
}
