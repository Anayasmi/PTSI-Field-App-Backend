package com.ptsi.report.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffSheetResponse {

    private String staffName;
    private Double staffId;
    private Double opening;
    private Double receipt;
    private Double expense;
    private Double closing;
    private Double tea;
    private Double telephone;
    private Double petrol;
}
