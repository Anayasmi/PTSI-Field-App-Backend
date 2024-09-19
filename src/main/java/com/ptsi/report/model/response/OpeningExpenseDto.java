package com.ptsi.report.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningExpenseDto {

    private Integer expenseId;
    private Float staffId;
    private Float openingExpense;
    private Float tea;
    private Float telephone;
    private Float petrol;
}
