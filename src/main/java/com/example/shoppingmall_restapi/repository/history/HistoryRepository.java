package com.example.shoppingmall_restapi.repository.history;

import com.example.shoppingmall_restapi.entity.history.History;
import com.example.shoppingmall_restapi.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Long> {
    List<History> findAllByMember(Member member);
    List<History> findAllBySeller(Member seller);
}
