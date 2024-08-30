package com.ptsi.report.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningExpenseResponse {

    private Float expenseId;
    private Float staffId;
    private String staffName;
    private Float openingExpense;
}
