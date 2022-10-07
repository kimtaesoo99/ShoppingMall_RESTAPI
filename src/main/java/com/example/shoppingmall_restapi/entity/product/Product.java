package com.example.shoppingmall_restapi.entity.product;



import com.example.shoppingmall_restapi.dto.product.ProductEditRequestDto;
import com.example.shoppingmall_restapi.entity.common.EntityDate;
import com.example.shoppingmall_restapi.entity.member.Member;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member seller;

    public Product(String name, String comment, int price, int quantity, Member seller) {
        this.name = name;
        this.comment = comment;
        this.price = price;
        this.quantity = quantity;
        this.seller = seller;
    }

    public void ProductEdit(ProductEditRequestDto req){
        this.name = req.getName();
        this.comment = req.getComment();
        this.price = req.getPrice();
        this.quantity = req.getQuantity();
    }

}
