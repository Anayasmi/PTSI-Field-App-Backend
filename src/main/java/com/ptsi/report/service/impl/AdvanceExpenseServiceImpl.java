package com.ptsi.report.service.impl;

import com.ptsi.report.entity.AdvanceRequestExpense;
import com.ptsi.report.entity.Staff;
import com.ptsi.report.model.request.AdvanceExpenseRequest;
import com.ptsi.report.model.response.AdvanceExpenseResponse;
import com.ptsi.report.model.response.CreationResponse;
import com.ptsi.report.repository.AdvanceExpenseRepository;
import com.ptsi.report.repository.StaffRepository;
import com.ptsi.report.service.AdvanceExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvanceExpenseServiceImpl implements AdvanceExpenseService {

    private final AdvanceExpenseRepository advanceExpenseRepository;
    private final StaffRepository staffRepository;

    @Override
    public CreationResponse updateAdvanceExpense( AdvanceExpenseRequest advanceExpenseRequest ) {

        AdvanceRequestExpense advanceRequestExpense =advanceExpenseRepository.findById( advanceExpenseRequest.getAdvanceRequestId()).orElseThrow();

        advanceRequestExpense.setApprovedAmount( advanceExpenseRequest.getApprovedAmount() );
        advanceRequestExpense.setAmountRequested( advanceExpenseRequest.getAmountRequested( ) );
        advanceRequestExpense.setApprovedBy( advanceExpenseRequest.getApprovedBy( ) );
        advanceRequestExpense.setApprovedOn( String.valueOf( LocalDate.now() ) );
        advanceExpenseRepository.save( advanceRequestExpense );
        return new CreationResponse(String.valueOf( advanceExpenseRequest.getAmountRequested() ),"Success");
    }

    @Override
    public List < AdvanceExpenseResponse > fetchAdvanceExpenses( ) {

        List < AdvanceRequestExpense > advanceRequestExpenses = advanceExpenseRepository.findAll( Sort.by( Sort.Direction.DESC,"createdOn" ) );
        List< Staff >  staffList=staffRepository.findAll();
        List < AdvanceExpenseResponse > advanceExpenseResponseList = new ArrayList <>( );
        advanceRequestExpenses.forEach( advanceRequestExpense ->
                advanceExpenseResponseList.add(
                        AdvanceExpenseResponse.builder( )
                                .advanceRequestId( advanceRequestExpense.getAdvanceRequestId( ) )
                                .createdBy( advanceRequestExpense.getCreatedBy( ) )
                                .staffName( fetchStaffName( staffList,advanceRequestExpense.getCreatedBy() ) )
                                .amountRequested( advanceRequestExpense.getAmountRequested( ) )
                                .approvedAmount( advanceRequestExpense.getApprovedAmount( ) )
                                .createdOn( advanceRequestExpense.getCreatedOn() )
                                .build( ) )
        );
        return advanceExpenseResponseList;
    }

    String fetchStaffName(List<Staff>staffList,Float staffId){

        Staff staff = staffList.stream( ).filter( e->e.getStaffId().equals( staffId ) ).toList( ).get( 0 );
        return staff.getFirstName( ).trim( ).concat( " " ).concat( staff.getLastName( ).trim( ) );

    }
}
