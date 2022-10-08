package com.example.shoppingmall_restapi.dto.review;

import com.example.shoppingmall_restapi.entity.review.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewFindResponseDto {

    private String user;
    private String content;
    private Integer rate;

    public static ReviewFindResponseDto toDto(Review review) {
        return new ReviewFindResponseDto(review.getMember().getNickname(),
                review.getContent(), review.getRate());
    }
}
