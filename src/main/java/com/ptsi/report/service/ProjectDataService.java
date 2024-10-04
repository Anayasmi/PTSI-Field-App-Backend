package com.ptsi.report.service;

import com.ptsi.report.model.request.ProjectRequest;
import com.ptsi.report.model.response.ProjectResponse;

import java.util.List;

public interface ProjectDataService {

    List< ProjectResponse > fetchProjectData(Float projectCoordinator);
    void updateProjectData( ProjectRequest projectRequest );
}
