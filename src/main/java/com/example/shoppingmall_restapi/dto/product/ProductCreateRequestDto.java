package com.example.shoppingmall_restapi.dto.product;

import com.example.shoppingmall_restapi.entity.image.Image;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    private List<MultipartFile> images = new ArrayList<>();
}
