package com.ptsi.report.controller;

import com.ptsi.report.model.request.ProjectRequest;
import com.ptsi.report.model.response.ProjectResponse;
import com.ptsi.report.service.ProjectDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin ( "*" )
@RequestMapping ( "/api/v1/project" )
@RequiredArgsConstructor
@Slf4j
public class ProjectDataController {

    private final ProjectDataService projectDataService;

    @GetMapping
    public ResponseEntity < List < ProjectResponse > > fetchProjectData ( ) {
        log.info( "Fetch all projects" );
        return new ResponseEntity <> ( projectDataService.fetchProjectData ( ) , HttpStatus.OK );
    }

    @PutMapping
    public ResponseEntity < ? > updateProjectData ( @RequestBody ProjectRequest projectRequest ) {
        log.info( "Update project with project coordinator  is {}",projectRequest.getProjectCoordinator() );
        projectDataService.updateProjectData ( projectRequest );
        return new ResponseEntity <> ( HttpStatus.ACCEPTED );
    }
}
