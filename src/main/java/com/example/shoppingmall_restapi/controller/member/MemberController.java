package com.example.shoppingmall_restapi.controller.member;

import com.example.shoppingmall_restapi.dto.member.MemberEditRequestDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.exception.MemberNotFoundException;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.response.Response;
import com.example.shoppingmall_restapi.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    //개인정보 조회
    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public Response memberFind(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        return Response.success(memberService.memberFind(member));
    }

    //회원정보 수정
    @PutMapping("members")
    @ResponseStatus(HttpStatus.OK)
    public Response memberEdit(@Valid @RequestBody MemberEditRequestDto req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        memberService.memberEdit(req,member);
        return Response.success();
    }
    //회원탈퇴
    @DeleteMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public Response memberDelete() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        memberService.memberDelete(member);
        return Response.success();
    }

    //좋아요한 상품 보기
    @GetMapping("/members/likes")
    @ResponseStatus(HttpStatus.OK)
    public Response likesFind() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        return Response.success(memberService.likesFind(member));
    }
}
