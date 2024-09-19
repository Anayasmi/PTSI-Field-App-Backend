package com.ptsi.report.model.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyExpenseRequest {
    private Integer expenseId;
    private Integer additionalStaff;
    private Integer year;
    private Integer month;
    private Float projectCoordinator;
    private Float staffId;
    private Float tea;
    private Float telephone;
    private Float petrol;
    private Integer updatedBy;
}
