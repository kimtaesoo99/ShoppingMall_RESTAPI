package com.example.shoppingmall_restapi.service;

import com.example.shoppingmall_restapi.dto.history.BuyHistoryFindResponseDto;
import com.example.shoppingmall_restapi.entity.history.History;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.repository.history.HistoryRepository;
import com.example.shoppingmall_restapi.service.history.HistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.shoppingmall_restapi.factory.HistoryFactory.createHistory;
import static com.example.shoppingmall_restapi.factory.MemberFactory.createMember;
import static com.example.shoppingmall_restapi.factory.MemberFactory.createSeller;
import static com.example.shoppingmall_restapi.factory.ProductFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceUnitTest {
    @InjectMocks
    HistoryService historyService;
    @Mock
    HistoryRepository historyRepository;

    @Test
    @DisplayName("구매기록")
    public void buyHistoryFindTest(){
        //given
        Member member = createMember();
        Member seller = createSeller();
        List<History> histories = new ArrayList<>();
        History history = createHistory(member,seller,createProduct(member));
        histories.add(history);
        given(historyRepository.findAllByMember(member)).willReturn(histories);

        //when
        List<BuyHistoryFindResponseDto> result = historyService.buyHistoryFind(member);

        //then
        assertThat(result.size()).isEqualTo(histories.size());
    }

    @Test
    @DisplayName("판매기록")
    public void sellHistoryFindTest(){
        //given
        Member member = createMember();
        Member seller = createSeller();
        List<History> histories = new ArrayList<>();
        History history = createHistory(member,seller,createProduct(member));
        histories.add(history);
        given(historyRepository.findAllBySeller(seller)).willReturn(histories);

        //when
        List<BuyHistoryFindResponseDto> result = historyService.sellHistoryFind(seller);

        //then
        assertThat(result.size()).isEqualTo(histories.size());
    }


}
