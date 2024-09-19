package com.ptsi.report.model.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningExpenses {

    private Double openingId;
    private LocalDate openingDate;
    private Double openingBalance;
    private LocalDate closingDate;
    private Double closingBalance;
}
