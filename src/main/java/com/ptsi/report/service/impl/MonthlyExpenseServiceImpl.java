package com.ptsi.report.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptsi.report.constant.StaffCategory;
import com.ptsi.report.entity.MonthlyExpense;
import com.ptsi.report.model.request.MonthlyExpenseRequest;
import com.ptsi.report.model.response.MonthlyExpenseResponse;
import com.ptsi.report.repository.MonthlyExpenseRepository;
import com.ptsi.report.service.MonthlyExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonthlyExpenseServiceImpl implements MonthlyExpenseService {

    private final MonthlyExpenseRepository monthlyExpenseRepository;

    @Override
    public void saveOrUpdateMonthlyExpense ( MonthlyExpenseRequest monthlyExpenseRequest ) {

        Integer createdBy=null;
        String createdDate=null;

        MonthlyExpense monthlyExpense = monthlyExpenseRepository.findByYearAndMonthAndStaffId ( monthlyExpenseRequest.getYear(), monthlyExpenseRequest.getMonth(),monthlyExpenseRequest.getStaffId () );

        if(monthlyExpense != null){
            monthlyExpenseRequest.setExpenseId( monthlyExpense.getExpenseId( ) );
            createdBy=monthlyExpense.getCreatedBy ();
            createdDate=monthlyExpense.getCreatedDate ();
        }else if(monthlyExpenseRequest.getExpenseId () != null){
            MonthlyExpense expense= monthlyExpenseRepository.findById ( monthlyExpenseRequest.getExpenseId () ).orElseThrow ();
            createdBy=expense.getCreatedBy ();
            createdDate=expense.getCreatedDate ();
        }

        monthlyExpenseRepository.save ( new MonthlyExpense ( monthlyExpenseRequest,createdBy,createdDate ) );
    }

    @Override
    public List < MonthlyExpenseResponse > fetchByProjectCoordinator ( Integer staffId, Integer year, Integer month, StaffCategory staffCategory ) {
        List< Map< String, Object > > openingExpenseList;

       if(staffCategory == StaffCategory.STAFF) {
            openingExpenseList = monthlyExpenseRepository.findStaffBYProjectCoordinator( staffId, year, month );
        }else {
            openingExpenseList = monthlyExpenseRepository.findAdditionalStaffBYProjectCoordinator( staffId, year, month );
        }

        return convertToDto (openingExpenseList);
    }

    public List<MonthlyExpenseResponse> convertToDto(List<Map<String, Object>> list) {
        return list.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private MonthlyExpenseResponse mapToDto(Map<String, Object> map) {
        ObjectMapper objectMapper= new ObjectMapper ();
        return objectMapper.convertValue(map, MonthlyExpenseResponse.class);
    }
}
