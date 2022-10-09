package com.example.shoppingmall_restapi.controller.history;

import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.exception.MemberNotFoundException;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.response.Response;
import com.example.shoppingmall_restapi.service.history.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HistoryController {

    private final HistoryService historyService;
    private final MemberRepository memberRepository;

    //사용자 구매기록보기
    @GetMapping("/buyHistories")
    @ResponseStatus(HttpStatus.OK)
    public Response buyHistoryFind() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        return Response.success(historyService.buyHistoryFind(member));
    }
    //판매자 판매목록보기
    @GetMapping("/sellHistories")
    @ResponseStatus(HttpStatus.OK)
    public Response sellHistoryFind(){
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        Member seller = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        return Response.success(historyService.sellHistoryFind(seller));
    }
}
