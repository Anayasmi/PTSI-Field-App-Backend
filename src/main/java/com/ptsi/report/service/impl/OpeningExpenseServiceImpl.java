package com.ptsi.report.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptsi.report.entity.MonthlyOpeningExpense;
import com.ptsi.report.model.request.OpeningExpenseRequest;
import com.ptsi.report.model.response.OpeningExpenseResponse;
import com.ptsi.report.repository.OpeningExpenseRepository;
import com.ptsi.report.repository.StaffRepository;
import com.ptsi.report.service.OpeningExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpeningExpenseServiceImpl implements OpeningExpenseService {

    private final OpeningExpenseRepository openingExpenseRepository;

    @Override
    public void saveOrUpdateOpeningExpense ( OpeningExpenseRequest openingExpenseRequest ) {

        Integer createdBy=null;
        String createdDate=null;

        MonthlyOpeningExpense monthlyOpeningExpense = openingExpenseRepository.findByYearAndMonthAndStaffId ( openingExpenseRequest.getYear (),openingExpenseRequest.getMonth (),openingExpenseRequest.getStaffId () );

        if(monthlyOpeningExpense != null){
            createdBy=monthlyOpeningExpense.getCreatedBy ();
            createdDate=monthlyOpeningExpense.getCreatedDate ();
        }else if(openingExpenseRequest.getExpenseId () != null){
            MonthlyOpeningExpense openingExpense= openingExpenseRepository.findById ( openingExpenseRequest.getExpenseId () ).orElseThrow ();
            createdBy=openingExpense.getCreatedBy ();
            createdDate=openingExpense.getCreatedDate ();
        }
        MonthlyOpeningExpense monthly=new MonthlyOpeningExpense ( openingExpenseRequest,createdBy,createdDate );
        openingExpenseRepository.save ( new MonthlyOpeningExpense ( openingExpenseRequest,createdBy,createdDate ) );
    }

    @Override
    public List < OpeningExpenseResponse > fetchByProjectCoordinator ( Integer staffId , Integer month , Integer year ) {

        List < Map < String, Object > > openingExpenseList=openingExpenseRepository.findByStaff ( staffId,month,year );
        return convertToDto (openingExpenseList  );
    }

    public List<OpeningExpenseResponse> convertToDto(List<Map<String, Object>> list) {
        return list.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private OpeningExpenseResponse mapToDto(Map<String, Object> map) {
        ObjectMapper objectMapper= new ObjectMapper ();
        return objectMapper.convertValue(map, OpeningExpenseResponse.class);
    }
}
