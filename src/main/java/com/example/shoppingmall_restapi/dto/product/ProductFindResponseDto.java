package com.example.shoppingmall_restapi.dto.product;

import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductFindResponseDto {
    private String name;
    private String comment;
    private int price;
    private int quantity;
    private String seller_username;
    private List<ImageDto> images;
    private LocalDateTime createAt;

    public static ProductFindResponseDto toDto(Product p) {
        return new ProductFindResponseDto(p.getName(), p.getComment(), p.getPrice(), p.getQuantity(), p.getSeller().getNickname(), p.getImages().stream().map(i -> ImageDto.toDto(i)).collect(Collectors.toList()),p.getCreatedAt());
    }
}
