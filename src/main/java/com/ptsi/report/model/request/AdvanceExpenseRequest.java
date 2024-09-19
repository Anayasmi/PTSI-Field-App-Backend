package com.ptsi.report.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvanceExpenseRequest {

    private Float advanceRequestId;

    private Float amountRequested;

    private Float approvedAmount;

    private Float approvedBy;

    private LocalDate approvedOn;

}
