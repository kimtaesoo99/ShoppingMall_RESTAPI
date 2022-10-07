package com.example.shoppingmall_restapi.service;

import com.example.shoppingmall_restapi.config.jwt.TokenProvider;
import com.example.shoppingmall_restapi.dto.sign.LoginRequestDto;
import com.example.shoppingmall_restapi.dto.sign.SignupRequestDto;
import com.example.shoppingmall_restapi.exception.LoginFailureException;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.repository.member.RefreshTokenRepository;
import com.example.shoppingmall_restapi.service.sign.SignService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.example.shoppingmall_restapi.factory.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SignServiceUnitTest {

    @InjectMocks
    SignService signService;

    @Mock
    AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    MemberRepository memberRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenProvider tokenProvider;
    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("유저 회원가입 서비스")
    void userSignupTest() {
        // given
        SignupRequestDto req = new SignupRequestDto("user", "user123!", "이름", "닉네임", "user@test.com" , "010-1111-1111", "경기도");

        // when
        signService.userSignup(req);

        // then
        verify(passwordEncoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("판매자 회원가입 서비스")
    void sellerSignupTest() {
        // given
        SignupRequestDto req = new SignupRequestDto("user", "user123!", "이름", "닉네임", "user@test.com" , "010-1111-1111", "경기도");

        // when
        signService.sellerSignup(req);

        // then
        verify(passwordEncoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void signInExceptionByNoneMemberTest() {
        // given
        given(memberRepository.findByUsername(any())).willReturn(Optional.of(createMember()));

        // when, then
        assertThatThrownBy(() -> signService.signIn(new LoginRequestDto("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    @DisplayName("패스워드 검증 테스트")
    void signInExceptionByInvalidPasswordTest() {
        // given
        given(memberRepository.findByUsername(any())).willReturn(Optional.of(createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.signIn(new LoginRequestDto("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }


}
