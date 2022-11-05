package com.example.shoppingmall_restapi.entity.report;

import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ProductReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportedProduct_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product reportedProduct;

    @Column(nullable = false)
    private String content;

    public ProductReport(Member reporter,Product reportedProduct,String content){
        this.reporter =reporter;
        this.reportedProduct =reportedProduct;
        this.content = content;
    }

}
