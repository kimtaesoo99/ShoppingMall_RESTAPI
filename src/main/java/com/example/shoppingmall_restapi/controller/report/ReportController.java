package com.example.shoppingmall_restapi.controller.report;


import com.example.shoppingmall_restapi.dto.report.MemberReportRequestDto;
import com.example.shoppingmall_restapi.dto.report.ProductReportRequest;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.exception.MemberNotFoundException;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.response.Response;
import com.example.shoppingmall_restapi.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {
    private final ReportService reportService;
    private final MemberRepository memberRepository;

    //유저를 신고
    @PostMapping("/report/users")
    @ResponseStatus(HttpStatus.OK)
    public Response reportMember(@Valid @RequestBody MemberReportRequestDto req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        reportService.reportMember(req,member);
        return Response.success();
    }

    //상품을 신고
    @PostMapping("/report/products")
    @ResponseStatus(HttpStatus.OK)
    public Response reportProduct(@Valid @RequestBody ProductReportRequest req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        reportService.reportProduct(req,member);
        return Response.success();
    }

}
