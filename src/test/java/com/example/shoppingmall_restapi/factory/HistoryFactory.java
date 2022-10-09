package com.example.shoppingmall_restapi.factory;

import com.example.shoppingmall_restapi.entity.history.History;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;

public class HistoryFactory {

    public static History createHistory(Member member, Member seller, Product product){
        return new History(member,seller,product,1);
    }
}
