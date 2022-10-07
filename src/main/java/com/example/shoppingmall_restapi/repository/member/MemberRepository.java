package com.example.shoppingmall_restapi.repository.member;


import com.example.shoppingmall_restapi.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByNickname(String nickname);
    public boolean existsByUsername(String username);
    public boolean existsByNickname(String nickname);
    public boolean existsByEmail(String Email);
    public boolean existsByPhone(String phone);
}
