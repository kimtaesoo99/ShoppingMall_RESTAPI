package com.example.shoppingmall_restapi.service;

import com.example.shoppingmall_restapi.dto.likes.LikeResponseDto;
import com.example.shoppingmall_restapi.dto.member.MemberEditRequestDto;
import com.example.shoppingmall_restapi.dto.member.MemberFindResponseDto;
import com.example.shoppingmall_restapi.entity.likes.Likes;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.repository.likes.LikesRepository;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.shoppingmall_restapi.factory.MemberFactory.createMember;
import static com.example.shoppingmall_restapi.factory.ProductFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceUnitTest {
    @InjectMocks
    MemberService memberService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    LikesRepository likesRepository;


    @Test
    @DisplayName("개인정보 조회")
    public void getMemberTest() {
        // given
        Member member = createMember();

        // when
        MemberFindResponseDto result = memberService.memberFind(member);

        // then
        assertThat(result.getName()).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("유저 정보 수정")
    public void editMemberTest() {
        // given
        MemberEditRequestDto req = new MemberEditRequestDto("이름 수정", "닉네임 수정", "010-1111-1111", "주소");
        Member member = createMember();
        given(memberRepository.existsByNickname(req.getNickname())).willReturn(false);
        given(memberRepository.existsByPhone(req.getPhone())).willReturn(false);

        // when
        memberService.memberEdit(req, member);

        // then
        assertThat(member.getName()).isEqualTo(req.getName());
    }

    @Test
    @DisplayName("회원 삭제")
    public void deleteMemberTest() {
        // given
        Member member = createMember();
        given(memberRepository.findByUsername(member.getUsername())).willReturn(Optional.of(member));

        // when
        memberService.memberDelete(member);

        // then
        verify(memberRepository).delete(member);
    }

    @Test
    @DisplayName("좋아요한 상품 조회")
    public void likesFind(){
        //given
        Member member = createMember();
        Likes likes = new Likes(member,createProduct(member));
        List<Likes> likesList = new ArrayList<>();
        likesList.add(likes);
        given(likesRepository.findAllByMember(member)).willReturn(likesList);

        //when
        List<LikeResponseDto> result = memberService.likesFind(member);


        //then
        assertThat(result.size()).isEqualTo(likesList.size());
    }
}
