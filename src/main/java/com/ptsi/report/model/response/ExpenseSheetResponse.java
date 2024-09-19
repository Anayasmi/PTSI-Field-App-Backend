package com.ptsi.report.model.response;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseSheetResponse {

    private LocalDate date;
    private Integer day;
    private Double openingExpense;
    private Double advance;
    private Double closingExpense;
    private Double totalExpense;
    private Double totalAdvance;
    private List<StaffSheetResponse> staffSheetResponseList;
    private Double tea;
    private Double telephone;
    private Double petrol;
}
