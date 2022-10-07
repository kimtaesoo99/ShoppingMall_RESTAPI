package com.example.shoppingmall_restapi.dto.product;

import com.example.shoppingmall_restapi.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductFindAllResponseDto {
    private Long product_id;
    private String name;
    private int price;
    private int quantity;
    private String seller_username;
    private LocalDateTime createdAt;

    public static ProductFindAllResponseDto toDto(Product p) {
        return new ProductFindAllResponseDto(p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getSeller().getName(), p.getCreatedAt());
    }
}