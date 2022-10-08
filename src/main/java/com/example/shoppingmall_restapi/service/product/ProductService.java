package com.example.shoppingmall_restapi.service.product;

import com.example.shoppingmall_restapi.dto.product.ProductCreateRequestDto;
import com.example.shoppingmall_restapi.dto.product.ProductEditRequestDto;
import com.example.shoppingmall_restapi.dto.product.ProductFindAllResponseDto;
import com.example.shoppingmall_restapi.dto.product.ProductFindResponseDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.exception.MemberNotEqualsException;
import com.example.shoppingmall_restapi.exception.ProductNotFoundException;
import com.example.shoppingmall_restapi.repository.product.ProductRepository;
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
@RequestMapping("api")
public class ProductService {

    private final ProductRepository productRepository;

    //상품 등록
    @Transactional
    public void productCreate(ProductCreateRequestDto req , Member member){
        Product product = new Product(req.getName(),req.getComment(),req.getPrice(), req.getQuantity(), member);
        productRepository.save(product);
    }
    //상품 전체조회
    @Transactional(readOnly = true)
    public List<ProductFindAllResponseDto> productFindAll(Pageable pageable){
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductFindAllResponseDto> result = new ArrayList<>();
        products.forEach(s-> result.add(ProductFindAllResponseDto.toDto(s)));
        return result;
    }
    //상품 단건 조회
    @Transactional(readOnly = true)
    public ProductFindResponseDto productFind(Long id){
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return ProductFindResponseDto.toDto(product);
    }
    //상품 수정
    @Transactional
    public void productEdit(ProductEditRequestDto req, Long id, Member member){
        Product product =productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        if (!member.equals(product.getSeller()))throw new MemberNotEqualsException();
        product.ProductEdit(req);
    }

    //상품 삭제
    @Transactional
    public void productDelete(Long id, Member member){
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        if (!member.equals(product.getSeller()))throw new MemberNotEqualsException();
        productRepository.delete(product);
    }
}
