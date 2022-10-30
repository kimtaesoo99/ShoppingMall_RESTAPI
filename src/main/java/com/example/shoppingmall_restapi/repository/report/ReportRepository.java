package com.example.shoppingmall_restapi.repository.report;

import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.report.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportRepository extends JpaRepository<UserReport,Long> {
    UserReport findByReporterAndReportedMember(Member reporter, Member reported);
}
