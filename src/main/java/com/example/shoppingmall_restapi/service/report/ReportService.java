package com.example.shoppingmall_restapi.service.report;


import com.example.shoppingmall_restapi.dto.report.MemberReportRequestDto;
import com.example.shoppingmall_restapi.dto.report.ProductReportRequest;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.entity.report.ProductReport;
import com.example.shoppingmall_restapi.entity.report.UserReport;
import com.example.shoppingmall_restapi.exception.*;
import com.example.shoppingmall_restapi.repository.member.MemberRepository;
import com.example.shoppingmall_restapi.repository.product.ProductRepository;
import com.example.shoppingmall_restapi.repository.report.ProductReportRepository;
import com.example.shoppingmall_restapi.repository.report.UserReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportService {
    private final UserReportRepository userReportRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    private final ProductReportRepository productReportRepository;
    //유저를 신고
    @Transactional
    public void reportMember(MemberReportRequestDto req, Member member) {
        Member reportedMember = memberRepository.findByNickname(req.getReportedName()).orElseThrow(MemberNotFoundException::new);
        if (member.equals(reportedMember)) throw new NotSelfReportException();
        if (userReportRepository.findByReporterAndReportedMember(member, reportedMember) != null) {
            throw new UserAlreadyReportedException();
        }

        UserReport userReport = new UserReport(member, reportedMember, req.getContent());
        userReportRepository.save(userReport);
        reportedMember.setReportedCount(reportedMember);


    }
    //상품을 신고
    @Transactional
    public void reportProduct(ProductReportRequest req,Member member){
        Product product = productRepository.findById(req.getProductId()).orElseThrow(ProductNotFoundException::new);
        if (member.equals(product.getSeller()))throw  new NotSelfReportException();
        if (productReportRepository.findByReporterAndReportedProduct(member,product)!=null){
            throw new ProductAlreadyReportedException();
        }

        ProductReport productReport = new ProductReport(member,product,req.getContent());
        productReportRepository.save(productReport);
        product.setReportedCount();

    }
}
