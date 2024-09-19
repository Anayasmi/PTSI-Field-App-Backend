package com.ptsi.report.model.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvanceExpenseDto {
    private Double staffId;
    private String staffName;
    private LocalDate date;
    private Double totalActualExpense;
    private Double amountCash;
    private Double approvedAmount;
}
