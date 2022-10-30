package com.example.shoppingmall_restapi.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberReportRequestDto {
    @NonNull
    private String reportedName;

    @NonNull
    private String content;


}
