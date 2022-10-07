package com.example.shoppingmall_restapi.dto.product;

import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductFindResponseDto {
    private String name;
    private String comment;
    private int price;
    private int quantity;
    private Member member;
    private LocalDateTime createAt;

    public static ProductFindResponseDto toDto(Product p) {
        return new ProductFindResponseDto(p.getName(), p.getComment(), p.getPrice(), p.getQuantity(), p.getSeller(), p.getCreatedAt());
    }
}
