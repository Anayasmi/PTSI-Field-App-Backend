package com.ptsi.report.model.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningExpenseResponse {

    private Double openingId;
    private Double staffId;
    private String staffName;
    private Integer check;
    private LocalDate openingDate;
    private Double openingBalance;
    private LocalDate closingDate;
    private Double closingBalance;
}
