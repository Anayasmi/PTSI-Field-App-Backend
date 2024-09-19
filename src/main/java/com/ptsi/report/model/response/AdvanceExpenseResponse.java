package com.ptsi.report.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvanceExpenseResponse {

    private Float advanceRequestId;

    private Float createdBy;

    private String staffName;

    private Float amountRequested;

    private Float approvedAmount;

}
