package com.ptsi.report.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningExpenseResponses {

    private Double staffId;
    private String staffName;
    private Double openingBalance;
    private Double closingBalance;
    private Integer check;
    private List<OpeningExpenses> openingExpensesList;
}
