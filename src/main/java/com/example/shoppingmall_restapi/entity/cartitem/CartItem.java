package com.example.shoppingmall_restapi.entity.cartitem;


import com.example.shoppingmall_restapi.entity.cart.Cart;
import com.example.shoppingmall_restapi.entity.common.EntityDate;
import com.example.shoppingmall_restapi.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
    @Column(nullable = false)
    private int quantity;

    public CartItem(Cart cart , Product product,int quantity){
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }
}
