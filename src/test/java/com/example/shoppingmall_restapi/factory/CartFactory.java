package com.example.shoppingmall_restapi.factory;

import com.example.shoppingmall_restapi.entity.cart.Cart;
import com.example.shoppingmall_restapi.entity.cartitem.CartItem;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;

public class CartFactory {
    public static Cart createCart(Member member){
        return new Cart(1L,member);
    }
    public static CartItem createCartItem(Cart cart, Product product){
        return new CartItem(cart,product,1);
    }
}
