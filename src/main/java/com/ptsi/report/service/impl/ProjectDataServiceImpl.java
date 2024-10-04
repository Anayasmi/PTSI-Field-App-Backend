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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectDataServiceImpl implements ProjectDataService {

    private final ProjectDataRepository projectDataRepository;
    private final StaffRepository staffRepository;

    @Override
    public List < ProjectResponse > fetchProjectData ( Float projectCoordinator) {
        List<ProjectData> projectData = projectDataRepository.findAll ();
        List<Float> staffs = projectData.stream ().map ( ProjectData::getProjectCoordinator ).distinct ().collect( Collectors.toList());
        List< Staff > staffList = staffRepository.findAllById ( staffs );
        Map <Float, String> staffMap = staffList.stream().collect(Collectors.toMap(Staff::getStaffId, staff -> staff.getFirstName ().trim ()+" "+staff.getLastName ().trim ()));
        List<ProjectResponse> projectResponses = projectData.stream( ).map ( e->  e.toDTO ( staffMap.get ( e.getProjectCoordinator () ) )).toList ();
        if(projectCoordinator!= null){
            projectResponses=projectResponses.stream ().filter ( e->Objects.equals ( e.getProjectCoordinator (),projectCoordinator ) ).toList ();
        }
        return projectResponses;
    }

    @Override
    public void updateProjectData ( ProjectRequest projectRequest ) {

        for(Long projectId:projectRequest.getProjectIds()) {

            ProjectData projectData = projectDataRepository.findById( projectId ).orElseThrow( );

            projectData.setProjectCoordinator( projectRequest.getProjectCoordinator( ) );
            projectData.setUpdatedBy( projectRequest.getUpdatedBy( ) );
            projectData.setUpdatedDate( LocalDate.now( ).format( DateTimeFormatter.ofPattern( "M/dd/yyyy" ) ) );
            projectDataRepository.save( projectData );
        }
    }
}
