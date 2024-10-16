package com.ptsi.report.service.impl;

import com.ptsi.report.entity.ProjectData;
import com.ptsi.report.entity.Staff;
import com.ptsi.report.model.response.*;
import com.ptsi.report.repository.ProjectDataRepository;
import com.ptsi.report.repository.StaffRepository;
import com.ptsi.report.model.request.StaffRequest;
import com.ptsi.report.service.CityService;
import com.ptsi.report.service.StaffService;
import com.ptsi.report.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final ZoneService zoneService;
    private final CityService cityService;
    private final ProjectDataRepository projectDataRepository;

    @Override
    public List < StaffResponse > fetchStaff ( Float projectCoordinator ) {
        List < Staff > staff = staffRepository.findActiveStaffByCoordinator ( );
        List < StaffResponse > staffResponseList = staff.stream ( )
                .map ( e -> e.toDto ( getStaff ( staff , e.getProjectCoordinator ( ) ) ) )
                .collect ( Collectors.toList ( ) );
        if ( projectCoordinator != null ) {
            staffResponseList = staffResponseList.stream ( ).filter ( e -> Objects.equals ( e.getProjectCoordinator ( ) , projectCoordinator ) ).toList ( );
        }
        return staffResponseList;
    }


    @Override
    public void updateStaff ( StaffRequest staffRequest ) {

        Staff staff = staffRepository.findById ( staffRequest.getStaffId ( ) ).orElseThrow ( );
        staff.setProjectCoordinator ( staffRequest.getProjectCoordinator ( ) );
        staff.setUpdatedBy ( Float.valueOf ( staffRequest.getUpdatedBy ( ) ) );
        staff.setUpdatedDate ( LocalDateTime.now ( ) );
        staffRepository.save ( staff );
    }

    @Override
    public List < StaffValue > fetchAllProCo ( ) {
        List < ZoneResponse > zoneResponseList = zoneService.fetchAllZones ( );
        return zoneResponseList.stream ( )
                .flatMap ( zone -> zone.getProjectCoordinators ( ).stream ( ) )
                .distinct ( )
                .toList ( );
    }

    @Override
    public List < StaffValue > findAllStaffNotInProjectCoordinator ( Float staffId ) {
        List < Map < String, Object > > list = staffRepository.findAllStaffNotInProjectCoordinator ( staffId );
        return list.stream ( ).map ( e -> new StaffValue ( setStaffId ( e.get ( "staffId" ) ) , ( String ) e.get ( "staffName" ) ) ).collect ( Collectors.toList ( ) );
    }

    @Override
    public Map < String, List < CommonResponse > > fetchAllRequiredData ( ) {
        List < StaffValue > projectCoordinators = fetchAllProCo ( );
        Map < String, List < CommonResponse > > response = new HashMap <> ( );
        List < CommonResponse > projectCoordinatorResponse = projectCoordinators.stream ( ).map ( e -> new CommonResponse ( String.valueOf ( e.getStaffId ( ).intValue ( ) ) , e.getStaffName ( ) , null ) ).distinct ( ).toList ( );
        response.put ( "coordinator" , projectCoordinatorResponse );

        List < Staff > staffList = staffRepository.findActiveStaffByCoordinator ( );
        List < CommonResponse > staffResponse = staffList.stream ( ).map ( e -> new CommonResponse ( setValue ( e.getStaffId ( ) ) , e.getFirstName ( ) + " " + e.getLastName ( ) , setValue ( e.getProjectCoordinator ( ) ) ) ).distinct ( ).toList ( );
        response.put ( "staff" , staffResponse );

        List < CityResponse > cityResponseList = cityService.fetchActiveCities ( );
        List < CommonResponse > cityResponse = cityResponseList.stream ( ).map ( e -> new CommonResponse ( String.valueOf ( e.getCityId ( ).intValue ( ) ) , e.getCityName ( ) , null ) ).distinct ( ).toList ( );
        response.put ( "city" , cityResponse );

        List < ProjectData > projectData = projectDataRepository.findAll ( );
        List < CommonResponse > projectResponse = projectData.stream ( ).map ( e -> new CommonResponse ( setLongValue ( e.getProjectId ( ) ) , e.getProjectName ( ) + "(" + e.getProjectNumber ( ) + ")" , null ) ).distinct ( ).toList ( );
        response.put ( "project" , projectResponse );

        return response;
    }

    String getStaff ( List < Staff > staffList , Float staffId ) {
        return staffList.stream ( )
                .filter ( e -> Objects.equals ( e.getStaffId ( ) , staffId ) )
                .findFirst ( )
                .map ( staff -> staff.getFirstName ( ).trim ( ) + " " + staff.getLastName ( ).trim ( )
                )
                .orElse ( null );
    }

    String setLongValue ( Long id ) {
        int value = id.intValue ( );
        return String.valueOf ( value );
    }

    String setValue ( Float id ) {
        int value = ( id == null ) ? 0 : id.intValue ( );
        return String.valueOf ( value );
    }

    Double setStaffId ( Object object ) {
        if ( object instanceof Integer ) {
            return Double.valueOf ( ( Integer ) object );
        }

        if ( object instanceof Double ) {
            return ( Double ) object;
        }

        if ( object instanceof Float ) {
            return Double.valueOf ( ( Float ) object );
        }
        return null;
    }
}
