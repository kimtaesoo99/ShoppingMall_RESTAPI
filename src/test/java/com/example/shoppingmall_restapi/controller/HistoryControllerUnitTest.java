package com.example.shoppingmall_restapi.controller;

import com.example.shoppingmall_restapi.controller.history.HistoryController;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.service.history.HistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static com.example.shoppingmall_restapi.factory.MemberFactory.createMember;
import static com.example.shoppingmall_restapi.factory.MemberFactory.createSeller;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class HistoryControllerUnitTest {
    @InjectMocks
    HistoryController historyController;
    @Mock
    HistoryService historyService;
    @Mock
    MemberRepository memberRepository;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(historyController).build();
    }

    @Test
    @DisplayName("구매기록조회")
    public void buyHistoryFindTest()throws Exception{
        //given
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                get("/api/buyHistories")
        ).andExpect(status().isOk());

        //then
        verify(historyService).buyHistoryFind(member);
    }

    @Test
    @DisplayName("판매기록조회")
    public void sellHistoryFindTest()throws Exception{
        //given
        Member seller = createSeller();
        Authentication authentication = new UsernamePasswordAuthenticationToken(seller.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(seller));

        //when
        mockMvc.perform(
                get("/api/sellHistories")
        ).andExpect(status().isOk());

        //then
        verify(historyService).sellHistoryFind(seller);
    }
}
