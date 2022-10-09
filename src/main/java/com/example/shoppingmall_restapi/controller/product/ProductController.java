package com.example.shoppingmall_restapi.controller.product;

import com.example.shoppingmall_restapi.dto.product.ProductCreateRequestDto;
import com.example.shoppingmall_restapi.dto.product.ProductEditRequestDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.exception.MemberNotFoundException;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.response.Response;
import com.example.shoppingmall_restapi.service.product.ProductService;
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
public class ProductController {
    private final ProductService productService;
    private final MemberRepository memberRepository;

    //상품 등록
    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Response productsCreate(@Valid @ModelAttribute ProductCreateRequestDto req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        productService.productCreate(req,member);
        return Response.success();
    }

    //상품 전체 조회
    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    public Response productFindAll(@PageableDefault(size=10,sort = "id",direction = Sort.Direction.DESC)Pageable pageable){
        return Response.success(productService.productFindAll(pageable));
    }

    //상품 단건 조회
    @GetMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response productFind(@PathVariable("id") Long id) {
        return Response.success(productService.productFind(id));
    }

    //상품 수정
    @PutMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response productEdit(@Valid @ModelAttribute ProductEditRequestDto req, @PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        productService.productEdit(req,id,member);
        return Response.success();
    }

    //상품 삭제
    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response productDelete(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        productService.productDelete(id,member);
        return Response.success();
    }
}
