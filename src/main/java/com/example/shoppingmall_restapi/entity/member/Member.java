package com.example.shoppingmall_restapi.entity.member;

import com.example.shoppingmall_restapi.dto.member.MemberEditRequestDto;
import com.example.shoppingmall_restapi.entity.common.EntityDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(nullable = false, length = 50)
    private String address;

    @Column(nullable = false)
    private int money;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String username, String password, String name, String nickname, String email, String phone, String address, Authority authority) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.authority = authority;
        this.money = 0;
    }

    public Member memberEdit(MemberEditRequestDto req){

        this.name =req.getName();
        this.nickname=req.getNickname();
        this.phone=req.getPhone();
        this.address=req.getAddress();
        return this;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
