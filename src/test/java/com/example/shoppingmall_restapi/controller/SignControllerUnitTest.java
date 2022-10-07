package com.example.shoppingmall_restapi.controller;


import com.example.shoppingmall_restapi.controller.sign.SignController;
import com.example.shoppingmall_restapi.dto.sign.LoginRequestDto;
import com.example.shoppingmall_restapi.dto.sign.ReissueRequestDto;
import com.example.shoppingmall_restapi.dto.sign.SignupRequestDto;
import com.example.shoppingmall_restapi.dto.sign.TokenResponseDto;
import com.example.shoppingmall_restapi.service.sign.SignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SignControllerUnitTest {
    @InjectMocks
    SignController signController;

    @Mock
    SignService signService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(signController).build();
    }

    @Test
    @DisplayName("유저 회원가입")
    void userSignupTest() throws Exception {
        // given
        SignupRequestDto req = new SignupRequestDto("user", "user123!", "이름", "닉네임", "user@test.com" , "010-1111-1111", "경기도");

        // when
        mockMvc.perform(
                        post("/api/sign-up/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // then
        verify(signService).userSignup(req);
    }

    @Test
    @DisplayName("판매자 회원가입")
    void guideSignupTest() throws Exception {
        // given
        SignupRequestDto req = new SignupRequestDto("user", "user123!", "이름", "닉네임", "user@test.com" , "010-1111-1111", "경기도");

        // when
        mockMvc.perform(
                        post("/api/sign-up/seller")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // then
        verify(signService).sellerSignup(req);
    }


    @Test
    @DisplayName("로그인")
    void signinTest() throws Exception {
        // given
        LoginRequestDto req = new LoginRequestDto("username", "password1!");
        given(signService.signIn(req)).willReturn(new TokenResponseDto("access", "refresh"));

        // when, then
        mockMvc.perform(
                        post("/api/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.accessToken").value("access"))
                .andExpect(jsonPath("$.result.data.refreshToken").value("refresh"));

        verify(signService).signIn(req);
    }


    @Test
    @DisplayName("토큰 재발급")
    void reissueTest() throws Exception {
        // given
        ReissueRequestDto req = new ReissueRequestDto("access", "refresh");

        // when, then
        mockMvc.perform(
                        post("/api/reissue")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

    }


    @Test
    @DisplayName("JSON 응답 확인")
    void ignoreNullValueInJsonResponseTest() throws Exception {
        // 응답결과로 반환되는 JSON 문자열이 올바르게 제거되는지 검증
        // given
        SignupRequestDto req = new SignupRequestDto("user", "user123!", "이름", "닉네임", "user@test.com" , "010-1111-1111", "경기도");

        // when, then
        mockMvc.perform(
                        post("/api/sign-up/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result").doesNotExist());

    }

}
