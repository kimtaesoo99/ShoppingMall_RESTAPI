package com.example.shoppingmall_restapi.dto.history;

import com.example.shoppingmall_restapi.entity.history.History;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyHistoryFindResponseDto {
    @NotBlank
    private String buyer;
    @NotBlank
    private String seller;
    @NotBlank
    private String productName;
    @NotNull
    private Integer productQuantity;
    private LocalDateTime createdAt;

    public static BuyHistoryFindResponseDto toDto(History history){
        return new BuyHistoryFindResponseDto(
                history.getMember().getName(),
                history.getSeller().getName(),
                history.getProduct().getName(),
                history.getProduct_quantity(),
                history.getCreatedAt()
        );
    }

}
