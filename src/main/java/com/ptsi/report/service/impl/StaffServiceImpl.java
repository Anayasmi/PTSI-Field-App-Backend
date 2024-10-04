package com.ptsi.report.service.impl;

import com.ptsi.report.entity.Staff;
import com.ptsi.report.model.response.StaffResponse;
import com.ptsi.report.model.response.StaffValue;
import com.ptsi.report.model.response.ZoneResponse;
import com.ptsi.report.repository.StaffRepository;
import com.ptsi.report.model.request.StaffRequest;
import com.ptsi.report.service.StaffService;
import com.ptsi.report.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final ZoneService zoneService;

    @Override
    public List < StaffResponse > fetchStaff ( Float projectCoordinator ) {
        List < Staff > staff = staffRepository.findActiveStaffByCoordinator ( );
        List<StaffResponse> staffResponseList = staff.stream()
                .map(e -> e.toDto(getStaff(staff, e.getProjectCoordinator())))
                .collect(Collectors.toList());
        if(projectCoordinator != null){
            staffResponseList=staffResponseList.stream ().filter ( e->Objects.equals ( e.getProjectCoordinator (),projectCoordinator) ).toList ();
        }
        return staffResponseList;
    }


    @Override
    public void updateStaff ( StaffRequest staffRequest ) {

        Staff staff = staffRepository.findById ( staffRequest.getStaffId ( ) ).orElseThrow ( );
        staff.setProjectCoordinator( staffRequest.getProjectCoordinator( ) );
        staff.setUpdatedBy ( Float.valueOf ( staffRequest.getUpdatedBy ( ) ) );
        staff.setUpdatedDate ( LocalDateTime.now ( ) );
        staffRepository.save ( staff );
    }
    @Override
    public List< StaffValue > fetchAllProCo(){
        List< ZoneResponse > zoneResponseList = zoneService.fetchAllZones();
        return  zoneResponseList.stream()
                .flatMap(zone -> zone.getProjectCoordinators().stream())
                .distinct()
                .toList();
    }

    @Override
    public List< StaffValue > findAllStaffNotInProjectCoordinator(Float staffId){
        List < Map<String,Object> > list = staffRepository.findAllStaffNotInProjectCoordinator ( staffId );
        return list.stream( ).map( e -> new StaffValue(  setStaffId (  e.get( "staffId" )),( String ) e.get( "staffName" ) ) ).collect( Collectors.toList( ) );
    }

    String getStaff( List < Staff > staffList , Float staffId ) {
        return staffList.stream( )
                .filter( e -> Objects.equals( e.getStaffId( ) , staffId ) )
                .findFirst( )
                .map( staff ->staff.getFirstName( ).trim( ) + " " + staff.getLastName( ).trim( )
                 )
                .orElse( null );
    }

    Double setStaffId(Object object){
        if(object instanceof Integer){
            return Double.valueOf (( Integer ) object);
        }

        if(object instanceof Double){
            return ( Double ) object;
        }

        if(object instanceof Float){
            return Double.valueOf (( Float ) object);
        }
        return null;
    }
}
