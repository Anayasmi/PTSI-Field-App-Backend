package com.ptsi.report.controller;

import com.ptsi.report.constant.StaffCategory;
import com.ptsi.report.constant.StaffType;
import com.ptsi.report.model.request.MonthlyExpenseRequest;
import com.ptsi.report.model.request.OpeningExpenseRequest;
import com.ptsi.report.model.response.MonthlyExpenseResponse;
import com.ptsi.report.model.response.OpeningExpenseResponse;
import com.ptsi.report.service.MonthlyExpenseService;
import com.ptsi.report.service.OpeningExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin ( "*" )
@RequestMapping ( "/api/v1/expense" )
@RequiredArgsConstructor
@Slf4j
public class MonthlyExpenseController {

    private final MonthlyExpenseService monthlyExpenseService;
    @PostMapping
    public ResponseEntity < ? > saveOrUpdateMonthlyExpense ( @RequestBody MonthlyExpenseRequest monthlyExpenseRequest ) {
        log.info( "save or update opening expense where staff id is {}",monthlyExpenseRequest.getStaffId() );
        monthlyExpenseService.saveOrUpdateMonthlyExpense ( monthlyExpenseRequest );
        return new ResponseEntity <> ( HttpStatus.ACCEPTED );
    }

    @GetMapping
    public ResponseEntity < List < MonthlyExpenseResponse > > fetchByProjectCoordinator ( @RequestParam Integer staffId, @RequestParam Integer month, @RequestParam Integer year, @RequestParam StaffCategory staffCategory ) {

        log.info( "Fetch opening expense where month {},year {}, staff id {}",month,year,staffId );
        return new ResponseEntity <> (monthlyExpenseService.fetchByProjectCoordinator ( staffId,year,month,staffCategory ),HttpStatus.ACCEPTED );
    }
}
