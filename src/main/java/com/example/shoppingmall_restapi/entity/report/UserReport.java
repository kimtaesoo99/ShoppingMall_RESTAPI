package com.example.shoppingmall_restapi.entity.report;


import com.example.shoppingmall_restapi.entity.member.Member;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member reportedMember;


    @Column(nullable = false)
    private String content;

    public UserReport(Member reporter, Member reportedMember, String content){
        this.reporter = reporter;
        this.reportedMember = reportedMember;
        this.content = content;
    }
}
