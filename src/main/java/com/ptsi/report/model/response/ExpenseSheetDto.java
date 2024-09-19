package com.ptsi.report.model.response;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseSheetDto {

    private String staffName;
    private Double staffId;
    private LocalDate date;
    private Double totalActualExpense;
    private Double approvedAmount;
    private Integer approvedBy;
    private Double amountCash;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private Double openingBalance;
    private Double closingBalance;
    private Double tea;
    private Double telephone;
    private Double petrol;
}
