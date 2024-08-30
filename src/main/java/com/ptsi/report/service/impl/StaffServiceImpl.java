package com.ptsi.report.service.impl;

import com.ptsi.report.entity.Staff;
import com.ptsi.report.model.response.StaffResponse;
import com.ptsi.report.repository.StaffRepository;
import com.ptsi.report.model.request.StaffRequest;
import com.ptsi.report.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        staff.setProjectCoordinator ( ( staffRequest.getProjectCoordinator ( ) == null ) ? staff.getProjectCoordinator ( ) : staffRequest.getProjectCoordinator ( ) );
        staff.setPetrol ( ( staffRequest.getPetrol ( ) == null ) ? staff.getPetrol ( ) : staffRequest.getPetrol ( ) );
        staff.setTea ( ( staffRequest.getTea ( ) == null ) ? staff.getTea ( ) : staffRequest.getTea ( ) );
        staff.setTelephone ( ( staffRequest.getTelephone ( ) == null ) ? staff.getTelephone ( ) : staffRequest.getTelephone ( ) );
        staff.setUpdatedBy ( Float.valueOf ( staffRequest.getUpdatedBy ( ) ) );
        staff.setUpdatedDate ( LocalDateTime.now ( ) );
        staffRepository.save ( staff );
    }
}
