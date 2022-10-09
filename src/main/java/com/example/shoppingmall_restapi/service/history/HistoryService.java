package com.example.shoppingmall_restapi.service.history;


import com.example.shoppingmall_restapi.dto.history.BuyHistoryFindResponseDto;
import com.example.shoppingmall_restapi.entity.history.History;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.repository.history.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@RequestMapping("/api")
public class HistoryService {
    private final HistoryRepository historyRepository;

    //구매자 기록보기
    @Transactional(readOnly = true)
    public List<BuyHistoryFindResponseDto> buyHistoryFind(Member member){
        List<History> histories = historyRepository.findAllByMember(member);
        List<BuyHistoryFindResponseDto> result =new ArrayList<>();
        histories.forEach(s->result.add(BuyHistoryFindResponseDto.toDto(s)));
        return  result;
    }
    //판매기록보기
    @Transactional(readOnly = true)
    public List<BuyHistoryFindResponseDto> sellHistoryFind(Member seller){
        List<History> histories = historyRepository.findAllBySeller(seller);
        List<BuyHistoryFindResponseDto> result =new ArrayList<>();
        histories.forEach(s->result.add(BuyHistoryFindResponseDto.toDto(s)));
        return  result;
    }


}
