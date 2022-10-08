package com.example.shoppingmall_restapi.repository.cart;

import com.example.shoppingmall_restapi.entity.cart.Cart;
import com.example.shoppingmall_restapi.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findCartByMember(Member member);
}
