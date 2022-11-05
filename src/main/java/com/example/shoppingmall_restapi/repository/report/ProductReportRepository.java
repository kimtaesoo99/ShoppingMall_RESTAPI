package com.example.shoppingmall_restapi.repository.report;

import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.entity.report.ProductReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductReportRepository extends JpaRepository<ProductReport,Long> {

    Product findByReporterAndReportedProduct(Member member, Product product);
}
