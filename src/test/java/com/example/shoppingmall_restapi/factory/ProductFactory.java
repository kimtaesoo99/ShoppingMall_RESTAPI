package com.example.shoppingmall_restapi.factory;

import com.example.shoppingmall_restapi.entity.image.Image;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;

import java.util.ArrayList;
import java.util.List;

import static com.example.shoppingmall_restapi.factory.ImageFactory.createImage;

public class ProductFactory {

    public static Product createProduct(Member member){
        List<Image> images = new ArrayList<>();
        images.add(createImage());
        Product product = new Product("name","comment",1,1,member,0,images);
        return product;
    }

    public static Product createProductWithImages(Member member, List<Image> images) {
        return new Product("제목", "내용",1,100,member,0,  images);
    }
}

