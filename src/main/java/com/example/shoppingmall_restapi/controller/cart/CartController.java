package com.example.shoppingmall_restapi.controller.cart;

import com.example.shoppingmall_restapi.dto.cart.CartCreateRequestDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.exception.MemberNotFoundException;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.response.Response;
import com.example.shoppingmall_restapi.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {
    private final CartService cartService;
    private final MemberRepository memberRepository;

    //장바구니 담기
    @PostMapping("/carts")
    @ResponseStatus(HttpStatus.CREATED)
    public Response cartCreate(@Valid @RequestBody CartCreateRequestDto req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        cartService.cartCreate(req,member);
        return Response.success();
    }

    //장바구니 조회
    @GetMapping("/carts")
    @ResponseStatus(HttpStatus.OK)
    public Response cartFindAll(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        return Response.success(cartService.cartFindAll(member));
    }

    //장바구니 품목 단건 삭제
    @DeleteMapping("/carts/{cartItemId}")
    @ResponseStatus(HttpStatus.OK)
    public Response cartDelete(@PathVariable("cartItemId") Long cartItemId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        cartService.cartDelete(cartItemId,member);
        return Response.success();
    }
    //장바구니 물건 전체 구매
    @PostMapping("/carts/buying")
    @ResponseStatus(HttpStatus.OK)
    public Response buying(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        cartService.buyingAll(member);
        return Response.success();
    }
}
