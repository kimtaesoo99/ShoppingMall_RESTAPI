package com.example.shoppingmall_restapi.controller;


import com.example.shoppingmall_restapi.controller.review.ReviewController;
import com.example.shoppingmall_restapi.dto.review.ReviewEditRequestDto;
import com.example.shoppingmall_restapi.dto.review.ReviewWriteRequestDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.entity.review.Review;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.repository.review.ReviewRepository;
import com.example.shoppingmall_restapi.service.review.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static com.example.shoppingmall_restapi.factory.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerUnitTest {
    @InjectMocks
    ReviewController reviewController;
    @Mock
    ReviewService reviewService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    ReviewRepository reviewRepository;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    //리뷰작성
    @Test
    @DisplayName("리뷰 생성")
    public void createReviewTest()throws Exception{
        //given
        ReviewWriteRequestDto req = new ReviewWriteRequestDto(1l,"a",4);
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                post("/api/reviews")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());


        //then
        verify(reviewService).reviewWrite(req,member);
    }

    //리뷰 조회
    @Test
    @DisplayName("리뷰 조회")
    public void getReviewTest()throws Exception{
        //given
        Pageable pageable = PageRequest.of(0,5, Sort.Direction.DESC,"id");
        Page<Review> result = reviewRepository.findAll(pageable);
        //when, //then
        assertThat(result).isEqualTo(null);

    }

    @Test
    @DisplayName("리뷰 수정")
    public void editReviewTest()throws Exception{
        //given
        Long id =1l;
        ReviewEditRequestDto req = new ReviewEditRequestDto(1l,"a",4);
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                put("/api/reviews/{id}",id)
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());


        //then
        verify(reviewService).reviewEdit(id,req,member);
    }

    @Test
    @DisplayName("리뷰 삭제")
    public void deleteReviewTest()throws Exception{
        //given
        Long reid =1l;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                delete("/api/reviews/{id}",reid)
        ).andExpect(status().isOk());


        //then
        verify(reviewService).reviewDelete(reid,member);
    }


}