package com.example.shoppingmall_restapi.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductReportRequest {
    @NotNull
    private Long productId;
    @NotBlank
    private String content;
}
