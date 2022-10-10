package com.example.shoppingmall_restapi.controller;


import com.example.shoppingmall_restapi.controller.member.MemberController;
import com.example.shoppingmall_restapi.dto.member.MemberEditRequestDto;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static com.example.shoppingmall_restapi.factory.MemberFactory.createMember;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerUnitTest {
    @InjectMocks
    MemberController memberController;
    @Mock
    MemberService memberService;
    @Mock
    MemberRepository memberRepository;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    @DisplayName("개인정보 조회")
    public void memberFindTest()throws Exception{
        //given
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));


        //when
        mockMvc.perform(
                get("/api/members")
        ).andExpect(status().isOk());

        //then
        verify(memberService).memberFind(member);
    }
    @Test
    @DisplayName("회원정보 수정")
    public void editMemberTest() throws Exception {
        // given
        MemberEditRequestDto req = new MemberEditRequestDto("이름 수정", "닉네임 수정", "010-1111-1111", "주소");
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when
        mockMvc.perform(
                put("/api/members")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        // then
        verify(memberService).memberEdit(req, member);
    }
    @Test
    @DisplayName("회원 삭제")
    public void deleteMemberTest() throws Exception {
        // given
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when
        mockMvc.perform(
                delete("/api/members")
        ).andExpect(status().isOk());

        // then
        verify(memberService).memberDelete(member);
    }

    @Test
    @DisplayName("좋아요한 상품 조회")
    public void likesFindTest()throws Exception{
        //given
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                get("/api/members/likes")
        ).andExpect(status().isOk());

        //then
        verify(memberService).likesFind(member);
    }
}
