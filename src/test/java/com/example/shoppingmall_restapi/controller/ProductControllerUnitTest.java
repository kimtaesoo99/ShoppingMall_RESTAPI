package com.example.shoppingmall_restapi.controller;

import com.example.shoppingmall_restapi.controller.product.ProductController;
import com.example.shoppingmall_restapi.dto.product.ProductCreateRequestDto;
import com.example.shoppingmall_restapi.dto.product.ProductEditRequestDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.repository.product.ProductRepository;
import com.example.shoppingmall_restapi.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.shoppingmall_restapi.factory.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
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
    @Mock
    ProductRepository productRepository;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @DisplayName("상품등록")
    public void productCreateTest()throws Exception{
        //given
        ArgumentCaptor<ProductCreateRequestDto> productCreateRequestDtoArgumentCaptor = ArgumentCaptor.forClass(ProductCreateRequestDto.class);
        List<MultipartFile> imageFiles = List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes()));
        ProductCreateRequestDto req = new ProductCreateRequestDto("제목","ㅁ" ,1,1, imageFiles);

        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when
        mockMvc.perform(
                        multipart("/api/products")
                                .file("images", imageFiles.get(0).getBytes())
                                .file("images", imageFiles.get(1).getBytes())
                                .param("name", req.getName())
                                .param("comment", req.getComment())
                                .param("price", String.valueOf(req.getPrice()))
                                .param("quantity", String.valueOf(req.getQuantity()))
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("POST");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        // then
        assertThat(req.getImages().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품 전체 조회")
    public void productFindAllTest()throws Exception{
        //given
        Pageable pageable = PageRequest.of(0,5, Sort.Direction.DESC,"id");
        Page<Product> result = productRepository.findAll(pageable);
        //when, //then
        assertThat(result).isEqualTo(null);

    }

    @Test
    @DisplayName("상품 단건 조회")
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
    @DisplayName("상품 수정")
    public void productEditTest()throws Exception {
        //given
        Long id = 1L;

        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<MultipartFile> addedImages = List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes())
        );
        List<Integer> deletedImages = List.of(1, 2);

        ProductEditRequestDto req = new ProductEditRequestDto("제목2", "내용2", 1,1, addedImages, deletedImages);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when
        mockMvc.perform(
                        multipart("/api/products/{id}", 1L)
                                .file("addedImages", addedImages.get(0).getBytes())
                                .file("addedImages", addedImages.get(1).getBytes())
                                .param("deletedImages", String.valueOf(deletedImages.get(0)), String.valueOf(deletedImages.get(1)))
                                .param("name", req.getName())
                                .param("comment", req.getComment())
                                .param("price", String.valueOf(req.getPrice()))
                                .param("quantity", String.valueOf(req.getQuantity()))
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("PUT");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        // then
        assertThat(req.getAddedImages().size()).isEqualTo(2);


    }

    @Test
    @DisplayName("상품 삭제")
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
