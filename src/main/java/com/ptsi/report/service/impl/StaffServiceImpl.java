package com.ptsi.report.service.impl;

import com.ptsi.report.entity.Staff;
import com.ptsi.report.model.response.ExpenseSheetDto;
import com.ptsi.report.model.response.StaffResponse;
import com.ptsi.report.model.response.StaffValue;
import com.ptsi.report.repository.StaffRepository;
import com.ptsi.report.model.request.StaffRequest;
import com.ptsi.report.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Override
    public List < StaffResponse > fetchStaff ( ) {
        List < Staff > staff = staffRepository.findAll ( );
        List < StaffResponse > staffResponseList = new ArrayList <> ( );
        staff.forEach ( e -> staffResponseList.add ( e.toDto ( ) ) );
        return staffResponseList;
    }


    @Override
    public void updateStaff ( StaffRequest staffRequest ) {

        Staff staff = staffRepository.findById ( staffRequest.getStaffId ( ) ).orElseThrow ( );

        staff.setUpdatedBy ( Float.valueOf ( staffRequest.getUpdatedBy ( ) ) );
        staff.setUpdatedDate ( LocalDateTime.now ( ) );
        staffRepository.save ( staff );
    }
    @Override
    public List< StaffValue > fetchAllProCo(){
        List < Map<String,Object> > list = staffRepository.findByProCo ( );
        return list.stream( ).map( e -> new StaffValue(  ( Double ) e.get( "staffId" ),( String ) e.get( "staffName" ) ) ).collect( Collectors.toList( ) );
    }

    @Override
    public List< StaffValue > findAllStaffNotInProjectCoordinator(Float staffId){
        List < Map<String,Object> > list = staffRepository.findAllStaffNotInProjectCoordinator ( staffId );
        return list.stream( ).map( e -> new StaffValue(  ( Double ) e.get( "staffId" ),( String ) e.get( "staffName" ) ) ).collect( Collectors.toList( ) );
    }
}
