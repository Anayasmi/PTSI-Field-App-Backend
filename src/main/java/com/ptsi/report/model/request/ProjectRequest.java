package com.ptsi.report.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {

    private List<Long> projectIds;
    private Float projectCoordinator;
    private Integer updatedBy;
}
