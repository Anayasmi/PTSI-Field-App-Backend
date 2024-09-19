package com.ptsi.report.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyExpenseResponse {

    private Float staffId;
    private String staffName;
    private Float tea;
    private Float telephone;
    private Float petrol;
}
