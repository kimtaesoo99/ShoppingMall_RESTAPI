package com.example.shoppingmall_restapi.service.review;

import com.example.shoppingmall_restapi.dto.review.ReviewEditRequestDto;
import com.example.shoppingmall_restapi.dto.review.ReviewFindResponseDto;
import com.example.shoppingmall_restapi.dto.review.ReviewWriteRequestDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.entity.review.Review;
import com.example.shoppingmall_restapi.exception.MemberNotEqualsException;
import com.example.shoppingmall_restapi.exception.ProductNotFoundException;
import com.example.shoppingmall_restapi.exception.ReviewNotFoundException;
import com.example.shoppingmall_restapi.repository.product.ProductRepository;
import com.example.shoppingmall_restapi.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    //리뷰 작성하기
    @Transactional
    public void reviewWrite(ReviewWriteRequestDto req, Member member){
        Product product = productRepository.findById(req.getProduct_Id()).orElseThrow(ProductNotFoundException::new);
        Review review = new Review(member,product,req.getContent(),req.getRate());
        reviewRepository.save(review);
    }

    //리뷰 보기
    @Transactional(readOnly = true)
    public List<ReviewFindResponseDto> reviewFind(Pageable pageable,Long id){
        Product product =productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        Page<Review> reviews = reviewRepository.findAllByProduct(product,pageable);
        List<ReviewFindResponseDto> result = new ArrayList<>();
        reviews.forEach(s->result.add(ReviewFindResponseDto.toDto(s)));
        return  result;
    }
    //리뷰 수정
    @Transactional
    public void reviewEdit(Long id, ReviewEditRequestDto req, Member member){
        Review review = reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
        if (!member.getNickname().equals(review.getMember().getNickname()))throw new MemberNotEqualsException();
        review.editReview(req.getContent(),req.getRate());
    }
    //리뷰 삭제
    @Transactional
    public void reviewDelete(Long id , Member member){
        Review review = reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
        if (!member.getNickname().equals(review.getMember().getNickname()))throw new MemberNotEqualsException();
        reviewRepository.delete(review);
    }
}
