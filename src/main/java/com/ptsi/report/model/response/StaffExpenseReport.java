package com.ptsi.report.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffExpenseReport {

    private String staffName;
    private Float opening;
    private Float receipt;
    private Float expense;
    private Float closing;
}
