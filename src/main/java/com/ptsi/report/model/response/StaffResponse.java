package com.ptsi.report.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffResponse {

    private Float staffId;
    private String employeeId;
    private String name;
    private String mobileNo;
    private String email;
    private Float projectCoordinator;
    private String projectCoordinatorName;
}
