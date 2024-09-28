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
import java.util.ArrayList;
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
    public List < StaffResponse > fetchStaff ( ) {
        List < Staff > staff = staffRepository.findActiveStaffByCoordinator ( );
        List < StaffResponse > staffResponseList = new ArrayList <> ( );
        staff.forEach ( e -> staffResponseList.add ( e.toDto ( getStaff( staff,e.getProjectCoordinator() )) ) );
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
        List<StaffValue> staffValueList = new ArrayList<>();
        zoneResponseList.forEach(zone -> staffValueList.addAll(zone.getProjectCoordinators()));
        return staffValueList.stream().distinct().toList();
    }

    @Override
    public List< StaffValue > findAllStaffNotInProjectCoordinator(Float staffId){
        List < Map<String,Object> > list = staffRepository.findAllStaffNotInProjectCoordinator ( staffId );
        return list.stream( ).map( e -> new StaffValue(  ( Double ) e.get( "staffId" ),( String ) e.get( "staffName" ) ) ).collect( Collectors.toList( ) );
    }

    String getStaff( List < Staff > staffList , Float staffId ) {
        return staffList.stream( )
                .filter( e -> Objects.equals( e.getStaffId( ) , staffId ) )
                .findFirst( )
                .map( staff ->staff.getFirstName( ).trim( ) + " " + staff.getLastName( ).trim( )
                 )
                .orElse( null );
    }
}
