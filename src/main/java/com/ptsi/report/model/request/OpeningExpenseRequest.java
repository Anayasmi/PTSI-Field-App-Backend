package com.ptsi.report.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningExpenseRequest {
    private Integer expenseId;
    private Integer month;
    private Integer year;
    private Float staffId;
    private Float openingExpense;
    private Integer updatedBy;
}
