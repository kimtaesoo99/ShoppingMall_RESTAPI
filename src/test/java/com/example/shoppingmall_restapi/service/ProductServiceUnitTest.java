package com.example.shoppingmall_restapi.service;

import com.example.shoppingmall_restapi.dto.product.ProductCreateRequestDto;
import com.example.shoppingmall_restapi.dto.product.ProductEditRequestDto;
import com.example.shoppingmall_restapi.dto.product.ProductFindAllResponseDto;
import com.example.shoppingmall_restapi.dto.product.ProductFindResponseDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.repository.product.ProductRepository;
import com.example.shoppingmall_restapi.service.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.shoppingmall_restapi.factory.MemberFactory.createMember;
import static com.example.shoppingmall_restapi.factory.ProductFactory.createProduct;
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

    @Test
    @DisplayName("productCreate")
    public void productCreateTest(){
        //given
        ProductCreateRequestDto req = new ProductCreateRequestDto("name","comment",1,1);
        Member member = createMember();

        //when
        productService.productCreate(req,member);

        //then
        verify(productRepository).save(any());
    }

    @Test
    @DisplayName("productFindAll")
    public void productFindAllTest(){
        //given
        Member member = createMember();
        Product product = createProduct(member);
        List<Product> products = new ArrayList<>();
        products.add(product);
        given(productRepository.findAll()).willReturn(products);


        //when
        List<ProductFindAllResponseDto> result = productService.productFindAll();

        //then
        assertThat(result.size()).isEqualTo(products.size());
    }
    @Test
    @DisplayName("productFind")
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
    @DisplayName("productEdit")
    public void productEditTest(){
        //given
        ProductEditRequestDto req  = new ProductEditRequestDto("name","comment",1,1);
        Long id =1l;
        Member member =createMember();
        Product product = createProduct(member);
        given(productRepository.findById(id)).willReturn(Optional.of(product));


        //when
        productService.productEdit(req,id, member);


        //then
        assertThat(req.getName()).isEqualTo(product.getName());
    }

    @Test
    @DisplayName("productDelete")
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
}
