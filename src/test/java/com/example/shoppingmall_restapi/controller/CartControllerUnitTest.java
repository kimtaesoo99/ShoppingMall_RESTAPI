package com.example.shoppingmall_restapi.controller;

import com.example.shoppingmall_restapi.controller.cart.CartController;
import com.example.shoppingmall_restapi.dto.cart.CartCreateRequestDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.service.cart.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static com.example.shoppingmall_restapi.factory.MemberFactory.createMember;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CartControllerUnitTest {
    @InjectMocks
    CartController cartController;
    @Mock
    CartService cartService;
    @Mock
    MemberRepository memberRepository;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }
    @Test
    @DisplayName("장바구니 담기")
    public void cartCreateTest()throws Exception{
        //given
        CartCreateRequestDto req = new CartCreateRequestDto(1l,1);
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                post("/api/carts")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());

        //then
        verify(cartService).cartCreate(req,member);
    }

    @Test
    @DisplayName("장바구니조회")
    public void cartFindAll()throws Exception{
        //given
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                get("/api/carts")
        ).andExpect(status().isOk());

        //then
        verify(cartService).cartFindAll(member);
    }

    @Test
    @DisplayName("장바구니 품목삭제")
    public void cartDeleteTest()throws Exception{
        //given
        Long cartItemId =1l;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));


        //when
        mockMvc.perform(
                delete("/api/carts/{cartItemId}",cartItemId)
        ).andExpect(status().isOk());

        //then
        verify(cartService).cartDelete(cartItemId,member);
    }

    @Test
    @DisplayName("전체구매")
    public void buyingTest()throws Exception{
        //given
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));


        //when
        mockMvc.perform(
                post("/api/carts/buying")
        ).andExpect(status().isOk());

        //then
        verify(cartService).buyingAll(member);
    }
}
