package com.example.shoppingmall_restapi.service;

import com.example.shoppingmall_restapi.dto.product.ProductCreateRequestDto;
import com.example.shoppingmall_restapi.dto.product.ProductEditRequestDto;
import com.example.shoppingmall_restapi.dto.product.ProductFindAllResponseDto;
import com.example.shoppingmall_restapi.dto.product.ProductFindResponseDto;
import com.example.shoppingmall_restapi.entity.likes.Likes;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.repository.likes.LikesRepository;
import com.example.shoppingmall_restapi.repository.product.ProductRepository;
import com.example.shoppingmall_restapi.service.image.FileService;
import com.example.shoppingmall_restapi.service.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.example.shoppingmall_restapi.factory.ImageFactory.createImage;
import static com.example.shoppingmall_restapi.factory.MemberFactory.createMember;
import static com.example.shoppingmall_restapi.factory.ProductFactory.createProduct;
import static com.example.shoppingmall_restapi.factory.ProductFactory.createProductWithImages;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {
    @InjectMocks
    ProductService productService;
    @Mock
    ProductRepository productRepository;
    @Mock
    FileService fileService;
    @Mock
    LikesRepository likesRepository;

    @Test
    @DisplayName("상품 등록")
    public void productCreateTest(){
        //given
        ProductCreateRequestDto req = new ProductCreateRequestDto("제목", "내용", 1,1, List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes()),
                new MockMultipartFile("test3", "test3.PNG", MediaType.IMAGE_PNG_VALUE, "test3".getBytes())
        ));


        Member member = createMember();

        given(productRepository.save(any())).willReturn(createProductWithImages(
                createMember(), IntStream.range(0, req.getImages().size()).mapToObj(i -> createImage()).collect(toList()))
        );

        // when
        productService.productCreate(req, member);

        // then
        verify(productRepository).save(any());


    }



    @Test
    @DisplayName("상품 전체 조회")
    public void productFindAllTest(){
        //given
        Pageable pageable = PageRequest.of(0,5, Sort.Direction.DESC,"id");
        List<Product> productList = productRepository.findAll();
        Member member = createMember();
        productList.add(createProduct(member));
        Page<Product> products = new PageImpl<>(productList);
        given(productRepository.findAll(pageable)).willReturn(products);


        //when
        List<ProductFindAllResponseDto> result = productService.productFindAll(pageable);

        //then
        assertThat(result.get(0).getName()).isEqualTo(createProduct(member).getName());
    }
    @Test
    @DisplayName("상품 단건 조회")
    public void productFindTest(){
        //given
        Long id =1l;
        Product product = createProduct(createMember());
        given(productRepository.findById(id)).willReturn(Optional.of(product));

        //when
        ProductFindResponseDto result = productService.productFind(id);

        //then
        assertThat(result.getName()).isEqualTo(product.getName());
    }

    @Test
    @DisplayName("상품 수정")
    public void productEditTest(){
        //given
        Long id = 1L;
        ProductEditRequestDto req = new ProductEditRequestDto("제목2", "내용2", 1,1, List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes()),
                new MockMultipartFile("test3", "test3.PNG", MediaType.IMAGE_PNG_VALUE, "test3".getBytes())
        ), List.of(1, 2));
        Member member = createMember();
        Product product = createProduct(member);
        given(productRepository.findById(id)).willReturn(Optional.of(product));

        // when
        productService.productEdit(req, id, member);

        // then
        assertThat(productRepository.findById(id).get().getComment()).isEqualTo("내용2");
    }


    @Test
    @DisplayName("상품 삭제")
    public void productDeleteTest(){
        //given
        Long id = 1l;
        Member member = createMember();
        Product product = createProduct(member);
        given(productRepository.findById(id)).willReturn(Optional.of(product));

        //when
        productService.productDelete(id,member);

        //then
        verify(productRepository).delete(product);
    }

    @Test
    @DisplayName("상품 좋아요 및 취소 (좋아요가 없는 경우)")
    public void likeProductTest() {
        // given
        Long id = 1L;
        Member member = createMember();
        Product product = createProduct(member);

        given(productRepository.findById(id)).willReturn(Optional.of(product));
        given(likesRepository.findByMemberAndProduct(member, product)).willReturn(Optional.empty());

        // when
        productService.productLike(id, member);

        // then
        verify(likesRepository).save(new Likes(member, product));
    }

    @Test
    @DisplayName("상품 좋아요 및 취소 (좋아요가 이미 있는 경우)")
    public void likeProductAlreadyLikeExistTest() {
        // given
        Long id = 1L;
        Member member = createMember();
        Product product = createProduct(member);
        Likes likes = new Likes(member, product);

        given(productRepository.findById(id)).willReturn(Optional.of(product));
        given(likesRepository.findByMemberAndProduct(member, product)).willReturn(Optional.of(likes));

        // when
        productService.productLike(id, member);

        // then
        verify(likesRepository).delete(likes);
    }
}
