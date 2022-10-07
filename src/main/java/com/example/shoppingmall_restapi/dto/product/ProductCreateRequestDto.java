package com.example.shoppingmall_restapi.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductCreateRequestDto {

    @NotBlank(message = "상품명을 입력해주세요.")
    private String name;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    private String comment;

    @NotNull(message = "상품 가격을 입력해주세요.")
    private int price;

    @NotNull(message = "상품 수량을 입력해주세요.")
    private int quantity;

}
