package com.ptsi.report.model.request;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningExpenseRequest {
    private Integer year;
    private Integer month;
    private Integer openingId;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private Float openingBalance;
    private Float staffId;
    private Float projectCoordinator;
    private Integer updatedBy;
    private Integer additionalStaff;
}
