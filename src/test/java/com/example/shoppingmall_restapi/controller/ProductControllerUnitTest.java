package com.example.shoppingmall_restapi.controller;

import com.example.shoppingmall_restapi.controller.product.ProductController;
import com.example.shoppingmall_restapi.dto.product.ProductCreateRequestDto;
import com.example.shoppingmall_restapi.dto.product.ProductEditRequestDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.service.product.ProductService;
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
public class ProductControllerUnitTest {
    @InjectMocks
    ProductController productController;
    @Mock
    ProductService productService;
    @Mock
    MemberRepository memberRepository;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @DisplayName("productCreate")
    public void productCreateTest()throws Exception{
        //given
        ProductCreateRequestDto req = new ProductCreateRequestDto("name","comment",1,1);
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                post("/api/products")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());

        //then
        verify(productService).productCreate(req,member);
    }

    @Test
    @DisplayName("productFindAll")
    public void productFindAllTest()throws Exception{
        //given

        //when
        mockMvc.perform(
                get("/api/products")
        ).andExpect(status().isOk());

        //then
        verify(productService).productFindAll();
    }

    @Test
    @DisplayName("productFind")
    public void productFindTest()throws Exception{
        //given
        Long id = 1l;

        //when
        mockMvc.perform(
                get("/api/products/{id}",id)
        ).andExpect(status().isOk());

        //then
        verify(productService).productFind(id);
    }

    @Test
    @DisplayName("productEdit")
    public void productEditTest()throws Exception {
        //given
        Long id = 1l;
        ProductEditRequestDto req = new ProductEditRequestDto("name", "commnet", 1, 1);
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));


        //when
        mockMvc.perform(
                put("/api/products/{id}", id)
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());


        //then
        verify(productService).productEdit(req,id,member);
    }

    @Test
    @DisplayName("productDelete")
    public void productDeleteTest()throws Exception{
        //given
        Long id =1l;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                delete("/api/products/{id}",id)
        ).andExpect(status().isOk());

        //then
        verify(productService).productDelete(id,member);
    }
}
