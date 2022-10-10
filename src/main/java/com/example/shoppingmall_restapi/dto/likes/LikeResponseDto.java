package com.example.shoppingmall_restapi.dto.likes;

import com.example.shoppingmall_restapi.entity.likes.Likes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LikeResponseDto {
    private Long productId;
    private String productName;

    public static LikeResponseDto toDto(Likes likes) {
        return new LikeResponseDto(likes.getProduct().getId(), likes.getProduct().getName());
    }
}
