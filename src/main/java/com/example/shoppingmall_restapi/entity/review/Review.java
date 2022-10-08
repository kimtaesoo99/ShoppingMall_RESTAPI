package com.example.shoppingmall_restapi.entity.review;

import com.example.shoppingmall_restapi.entity.common.EntityDate;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.repository.review.ReviewRepository;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Review extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Max(5)
    @Min(1)
    private Integer rate;

    public Review(Member member,Product product,String content,Integer rate){
        this.member= member;
        this.product =product;
        this.content =content;
        this.rate =rate;
    }
    public Review editReview(String content, Integer rate){
        this.content = content;
        this.rate =rate;
        return this;
    }
}
