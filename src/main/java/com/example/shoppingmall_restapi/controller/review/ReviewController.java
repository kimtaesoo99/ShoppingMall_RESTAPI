package com.example.shoppingmall_restapi.controller.review;


import com.example.shoppingmall_restapi.dto.review.ReviewEditRequestDto;
import com.example.shoppingmall_restapi.dto.review.ReviewWriteRequestDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.exception.MemberNotFoundException;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.response.Response;
import com.example.shoppingmall_restapi.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    private final MemberRepository memberRepository;
    //리뷰 작성하기
    @PostMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Response reviewWrite(@Valid @RequestBody ReviewWriteRequestDto req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        reviewService.reviewWrite(req,member);
        return Response.success();
    }

    //리뷰 보기
    @GetMapping("/products/{id}/reviews")
    @ResponseStatus(HttpStatus.OK)
    public Response reviewFind(@PageableDefault(size = 10,sort = "id",direction = Sort.Direction.DESC ) Pageable pageable,
            @PathVariable("id") Long id){

        return Response.success(reviewService.reviewFind(pageable,id));
    }

    //리뷰 수정
    @PutMapping("/reviews/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response reviewEdit(@PathVariable("id") Long id
    ,@Valid @RequestBody ReviewEditRequestDto req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        reviewService.reviewEdit(id,req,member);
        return Response.success();
    }
    //리뷰 삭제
    @DeleteMapping("/reviews/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response reviewDelete(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        reviewService.reviewDelete(id,member);
        return Response.success();
    }
}
