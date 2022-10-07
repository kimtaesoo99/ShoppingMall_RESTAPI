package com.example.shoppingmall_restapi.factory;

import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;

public class ProductFactory {

    public static Product createProduct(Member member){
        Product product = new Product("name","comment",1,1,member);
        return product;
    }
}

