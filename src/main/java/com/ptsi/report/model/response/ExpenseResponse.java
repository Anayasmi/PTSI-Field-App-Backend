package com.ptsi.report.model.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {

    private Integer date;
    private String openingAdvance;
    private List< StaffExpenseReport > staffExpenseReportList;
}
