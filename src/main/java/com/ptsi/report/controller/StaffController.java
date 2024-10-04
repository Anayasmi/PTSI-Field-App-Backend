package com.ptsi.report.controller;

import com.ptsi.report.model.request.StaffRequest;
import com.ptsi.report.model.response.StaffResponse;
import com.ptsi.report.model.response.StaffValue;
import com.ptsi.report.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin ( "*" )
@RequestMapping ( "/api/v1/staff" )
@RequiredArgsConstructor
@Slf4j
public class StaffController {

    private final StaffService staffService;

    @GetMapping
    public ResponseEntity < List < StaffResponse > > fetchStaff (@RequestParam(required = false) Float projectCoordinator ) {
        log.info( "Fetch all staffs" );
        return new ResponseEntity <> ( staffService.fetchStaff ( projectCoordinator ) , HttpStatus.OK );
    }

    @PutMapping
    public ResponseEntity < ? > updateStaff ( @RequestBody StaffRequest staffRequest ) {
        log.info( "Update staff where staff id is {}",staffRequest.getStaffId() );
        staffService.updateStaff ( staffRequest );
        return new ResponseEntity <> ( HttpStatus.ACCEPTED );
    }

    @GetMapping("/pro_co")
    public ResponseEntity<List< StaffValue >> fetchAllProCo(){
        log.info( "Fetch all staffs" );
        return new ResponseEntity <> ( staffService.fetchAllProCo ( ) , HttpStatus.OK );
    }

    @GetMapping("/not-in-pro-co")
    public ResponseEntity<List< StaffValue >> findAllStaffNotInProjectCoordinator(@RequestParam Float staffId){
        log.info( "Fetch all staffs where project coordinator {} is not there",staffId );
        return new ResponseEntity <> ( staffService.findAllStaffNotInProjectCoordinator ( staffId ) , HttpStatus.OK );
    }


}
