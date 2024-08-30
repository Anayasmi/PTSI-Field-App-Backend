package com.ptsi.report.service.impl;

import com.ptsi.report.entity.ProjectData;
import com.ptsi.report.entity.Staff;
import com.ptsi.report.model.response.ProjectResponse;
import com.ptsi.report.repository.ProjectDataRepository;
import com.ptsi.report.model.request.ProjectRequest;
import com.ptsi.report.repository.StaffRepository;
import com.ptsi.report.service.ProjectDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectDataServiceImpl implements ProjectDataService {

    private final ProjectDataRepository projectDataRepository;
    private final StaffRepository staffRepository;

    @Override
    public List < ProjectResponse > fetchProjectData ( ) {
        List<ProjectData> projectData = projectDataRepository.findAll ();
        List<Float> staffs = projectData.stream ().map ( ProjectData::getProjectCoordinator ).distinct ().collect( Collectors.toList());
        List< Staff > staffList = staffRepository.findAllById ( staffs );
        List<ProjectResponse> projectResponses=new ArrayList <> ();
        projectData.forEach ( e-> {

                List < Staff > staffLists = staffList.stream ( ).filter ( staff -> Objects.equals ( staff.getStaffId ( ) , e.getProjectCoordinator ( ) ) ).collect ( Collectors.toList ( ) );
                String staffName = ( e.getProjectCoordinator () != null && staffLists.size ( ) > 0 ) ? staffLists.get ( 0 ).getFirstName ( ).trim () + " " + staffLists.get ( 0 ).getLastName ( ).trim () : null;
                projectResponses.add ( e.toDTO ( staffName ) );
        });
        return projectResponses;
    }

    @Override
    public void updateProjectData ( ProjectRequest projectRequest ) {

        ProjectData projectData = projectDataRepository.findById ( projectRequest.getProjectId () ).orElseThrow ();

        projectData.setProjectCoordinator ( projectRequest.getProjectCoordinator () );
        projectData.setUpdatedBy ( projectRequest.getUpdatedBy () );
        projectData.setUpdatedDate ( LocalDate.now ( ).format ( DateTimeFormatter.ofPattern("M/dd/yyyy" ) ));
        projectDataRepository.save ( projectData );
    }
}
