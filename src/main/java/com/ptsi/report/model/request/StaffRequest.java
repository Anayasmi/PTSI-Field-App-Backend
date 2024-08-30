package com.ptsi.report.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffRequest {

    private Float staffId;
    private Float projectCoordinator;
    private Float tea;
    private Float telephone;
    private Float petrol;
    private Integer updatedBy;
}
