package com.ptsi.report.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {

    private Long projectId;
    private String projectNumber;
    private String projectName;
    private Float projectCoordinator;
    private String project;
    private String projectCoordinatorName;
}
