package com.example.shoppingmall_restapi.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWriteRequestDto {
    @NotNull
    private Long product_Id;

    @NotBlank
    private String content;

    @NotNull
    private Integer rate;
}
