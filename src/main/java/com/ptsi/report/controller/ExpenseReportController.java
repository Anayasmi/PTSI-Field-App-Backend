package com.ptsi.report.controller;

import com.ptsi.report.constant.StaffType;
import com.ptsi.report.model.response.ExpenseResponse;
import com.ptsi.report.model.response.ExpenseSheetDto;
import com.ptsi.report.model.response.ExpenseSheetResponse;
import com.ptsi.report.service.impl.ExpenseReportServiceImpl;
import com.ptsi.report.util.ExpenseSheetExcel;
import com.ptsi.report.util.StaffSheetExcel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin ( "*" )
@RequestMapping ( "/api/v1/report" )
@RequiredArgsConstructor
@Slf4j
public class ExpenseReportController {

    private final ExpenseReportServiceImpl expenseReportService;

    @GetMapping
    public ResponseEntity < Resource > fetchExpenseReport (
            @RequestParam Integer year,@RequestParam Integer month,
            @RequestParam Float staffId , @RequestParam StaffType staffType ) {

        log.info( "Fetch expense report from {} to {}",month,year );

        List < Map < String, Object > > report = expenseReportService.fetchExpenseReport ( year,month , staffId , staffType );

        String fileName = ( staffType == StaffType.PRO_CO ) ? "project_coordinator_sheet_" + staffId + ".xlsx" : "staff_sheet_" + staffId + ".xlsx";
        log.info( "Excel formatting started");
        ByteArrayInputStream byteArrayInputStream = StaffSheetExcel.dataToExcel ( report,staffType );
        assert byteArrayInputStream != null;
        InputStreamResource file = new InputStreamResource ( byteArrayInputStream );
        return ResponseEntity.ok ( ).header ( HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=" + fileName )
                .contentType ( MediaType.parseMediaType ( "application/vnd.ms-excel" ) )
                .body ( file );
    }

    @GetMapping("expense-sheet")
    public ResponseEntity < Resource > downloadExpenseSheet (
            @RequestParam Integer year,@RequestParam Integer month,
            @RequestParam Integer staffId,@RequestParam Double openingBalance ) {

        log.info( "Fetch expense report where month is {} and year is {}",month,year );

        List < ExpenseSheetResponse > report = expenseReportService.fetchExpenseSheet ( year,month, staffId,openingBalance);

        String fileName =  "project_coordinator_sheet_" + staffId + ".xlsx";
        log.info( "Excel formatting started");
        ByteArrayInputStream byteArrayInputStream = ExpenseSheetExcel.dataToExcel ( report );
        assert byteArrayInputStream != null;
        InputStreamResource file = new InputStreamResource ( byteArrayInputStream );
        return ResponseEntity.ok ( ).header ( HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=" + fileName )
                .contentType ( MediaType.parseMediaType ( "application/vnd.ms-excel" ) )
                .body ( file );
    }

    @GetMapping("get")
    ResponseEntity<List <ExpenseSheetResponse>> fetchByProjectCoordinator (
            @RequestParam Integer year,@RequestParam Integer month,
            @RequestParam Integer staffId ){
        return new ResponseEntity<>(expenseReportService.fetchByProjectCoordinator(year,month,staffId), HttpStatus.OK);
    }
}
