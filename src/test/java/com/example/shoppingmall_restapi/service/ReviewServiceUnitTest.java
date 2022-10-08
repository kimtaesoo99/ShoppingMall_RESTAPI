package com.example.shoppingmall_restapi.service;

import com.example.shoppingmall_restapi.dto.review.ReviewEditRequestDto;
import com.example.shoppingmall_restapi.dto.review.ReviewFindResponseDto;
import com.example.shoppingmall_restapi.dto.review.ReviewWriteRequestDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.entity.review.Review;
import com.example.shoppingmall_restapi.repository.product.ProductRepository;
import com.example.shoppingmall_restapi.repository.review.ReviewRepository;
import com.example.shoppingmall_restapi.service.review.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static com.example.shoppingmall_restapi.factory.MemberFactory.createMember;
import static com.example.shoppingmall_restapi.factory.ProductFactory.createProduct;
import static com.example.shoppingmall_restapi.factory.ReviewFactory.createReview;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceUnitTest {
    @InjectMocks
    ReviewService reviewService;
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("리뷰 작성")
    public void createReviewTest(){
        //given
        ReviewWriteRequestDto req = new ReviewWriteRequestDto(1l,"a",2);
        Member member =createMember();
        Product product =createProduct(member);
        Review review = createReview(member,product);
        given(productRepository.findById(req.getProduct_Id())).willReturn(Optional.of(product));

        //when
        reviewService.reviewWrite(req,member);

        //then
        verify(reviewRepository).save(any());
    }
    @Test
    @DisplayName("리뷰수정")
    public void editReviewTest(){
        //given
        Long id =1l;
        ReviewEditRequestDto req = new ReviewEditRequestDto(1l,"a",2);
        Member member =createMember();
        Product product =createProduct(member);
        Review review = createReview(member,product);
        given(reviewRepository.findById(id)).willReturn(Optional.of(review));

        //when
        reviewService.reviewEdit(id,req,member);

        //then
        assertThat(review.getContent()).isEqualTo(req.getContent());
    }

    @Test
    @DisplayName("리뷰 조회")
    public void findReviewTest(){
        //given
        Long id =1l;
        Pageable pageable = PageRequest.of(0,5, Sort.Direction.DESC,"id");
        Member member =createMember();
        Product product = createProduct(member);
        Review review = createReview(member,product);
        List<Review> revie = new ArrayList<>();
        revie.add(review);
        Page<Review> reviews = new PageImpl<>(revie);

        given(productRepository.findById(id)).willReturn(Optional.of(product));
        given(reviewRepository.findAllByProduct(product,pageable)).willReturn(reviews);

        //when
        List<ReviewFindResponseDto> result = reviewService.reviewFind(pageable,id);

        //then
        assertThat(result.get(0).getContent()).isEqualTo(review.getContent());
    }

    @Test
    @DisplayName("리뷰 삭제")
    public void deleteReviewTest(){
        //given
        Long id =1l;
        Member member =createMember();
        Product product =createProduct(member);
        Review review = createReview(member,product);
        given(reviewRepository.findById(id)).willReturn(Optional.of(review));

        //when
        reviewService.reviewDelete(id,member);

        //then
        verify(reviewRepository).delete(review);
    }

}
