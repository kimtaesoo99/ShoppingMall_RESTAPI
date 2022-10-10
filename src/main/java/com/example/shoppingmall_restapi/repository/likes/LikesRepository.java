package com.example.shoppingmall_restapi.repository.likes;

import com.example.shoppingmall_restapi.entity.likes.Likes;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes,Long> {

    Optional<Likes> findByMemberAndProduct(Member member, Product product);
    List<Likes> findAllByMember(Member member);
}
