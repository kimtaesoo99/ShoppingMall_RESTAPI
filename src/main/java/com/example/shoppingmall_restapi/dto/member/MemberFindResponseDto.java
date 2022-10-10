package com.example.shoppingmall_restapi.dto.member;

import com.example.shoppingmall_restapi.entity.member.Authority;
import com.example.shoppingmall_restapi.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberFindResponseDto {
    private String username;

    private String name;

    private String nickname;


    private String email;

    private String phone;

    private String address;

    private int money;

    private Authority authority;

    public MemberFindResponseDto toDto(Member m) {
        return new MemberFindResponseDto(m.getUsername(), m.getName(), m.getNickname(),  m.getEmail(), m.getPhone(), m.getAddress(), m.getMoney(), m.getAuthority());
    }
}
