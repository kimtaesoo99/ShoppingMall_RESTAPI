package com.example.shoppingmall_restapi.service.member;

import com.example.shoppingmall_restapi.dto.likes.LikeResponseDto;
import com.example.shoppingmall_restapi.dto.member.MemberEditRequestDto;
import com.example.shoppingmall_restapi.dto.member.MemberFindResponseDto;
import com.example.shoppingmall_restapi.entity.likes.Likes;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.exception.MemberNicknameAlreadyExistsException;
import com.example.shoppingmall_restapi.exception.MemberNotFoundException;
import com.example.shoppingmall_restapi.exception.MemberPhoneAlreadyExistsException;
import com.example.shoppingmall_restapi.repository.likes.LikesRepository;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberService {
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;

    //개인정보 조회
    @Transactional(readOnly = true)
    public MemberFindResponseDto memberFind(Member member){
        return new MemberFindResponseDto().toDto(member);
    }

    //회원정보 수정
    @Transactional
    public void memberEdit(MemberEditRequestDto req, Member member) {

        if (memberRepository.existsByNickname(req.getNickname())) {
            if (!req.getNickname().equals(member.getNickname())) {
                throw new MemberNicknameAlreadyExistsException(req.getNickname());
            }
        }

        if (memberRepository.existsByPhone(req.getPhone())) {
            if (!req.getPhone().equals(member.getPhone())) {
                throw new MemberPhoneAlreadyExistsException(req.getPhone());
            }
        }
        member.memberEdit(req);
    }
    //회원탈퇴
    @Transactional
    public void memberDelete(Member member){
        if (memberRepository.findByUsername(member.getUsername()).isEmpty())
            throw new MemberNotFoundException();
        memberRepository.delete(member);
    }

    //좋아요한 상품 보기
    @Transactional(readOnly = true)
    public List<LikeResponseDto> likesFind(Member member) {
        List<Likes> likes = likesRepository.findAllByMember(member);
        List<LikeResponseDto> result = new ArrayList<>();

        likes.stream().forEach(like -> result.add(new LikeResponseDto().toDto(like)));
        return result;
    }
}


