package com.ptsi.report.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {

    private Long projectId;
    private Float projectCoordinator;
    private Integer updatedBy;
}
