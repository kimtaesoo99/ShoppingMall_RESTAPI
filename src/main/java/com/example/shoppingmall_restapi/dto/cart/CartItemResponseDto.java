package com.example.shoppingmall_restapi.dto.cart;


import com.example.shoppingmall_restapi.entity.cartitem.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemResponseDto {

    private Long cartItemId;
    private String name;
    private Integer InsertQuantity;
    private Integer price;

    public static CartItemResponseDto toDto(CartItem cartItem, String name, int price) {
        return new CartItemResponseDto(cartItem.getId(), name, cartItem.getQuantity(), price);
    }
}