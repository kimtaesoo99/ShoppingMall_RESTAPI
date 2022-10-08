package com.example.shoppingmall_restapi.factory;


import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.entity.review.Review;

public class ReviewFactory {

    public static Review createReview(Member member, Product product){
        Review review = new Review(member,product,"a",1);
        return review;
    }
}
