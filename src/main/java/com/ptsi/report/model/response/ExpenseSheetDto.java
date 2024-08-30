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
    private Double openingExpense;
    private Double totalActualExpense;
    private Integer approvedAmount;
    private Double tea;
    private Double telephone;
    private Double petrol;
}
