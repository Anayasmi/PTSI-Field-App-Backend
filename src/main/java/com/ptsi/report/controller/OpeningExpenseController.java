package com.ptsi.report.controller;

import com.ptsi.report.constant.StaffCategory;
import com.ptsi.report.model.request.OpeningExpenseRequest;
import com.ptsi.report.model.response.OpeningExpenseResponse;
import com.ptsi.report.model.response.OpeningExpenseResponses;
import com.ptsi.report.service.OpeningExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin ( "*" )
@RequestMapping ( "/api/v1/opening" )
@RequiredArgsConstructor
@Slf4j
public class OpeningExpenseController {

    private final OpeningExpenseService openingExpenseService;
    @PostMapping
    public ResponseEntity < ? > saveOrUpdateOpeningExpense ( @RequestBody OpeningExpenseRequest openingExpenseRequest ) {
        log.info( "save or update opening expense where staff id is {}",openingExpenseRequest.getStaffId() );
        openingExpenseService.saveOrUpdateOpeningExpense ( openingExpenseRequest );
        return new ResponseEntity <> ( HttpStatus.ACCEPTED );
    }

    @GetMapping
    public ResponseEntity < List< OpeningExpenseResponses >  > fetchByProjectCoordinator ( @RequestParam Integer staffId, @RequestParam Integer month, @RequestParam Integer year, @RequestParam StaffCategory staffCategory ) {

        log.info( "Fetch opening expense where month {},year {}, staff id {}",month,year,staffId );
        return new ResponseEntity <> (openingExpenseService.fetchByProjectCoordinator ( staffId,month,year,staffCategory ),HttpStatus.ACCEPTED );
    }

    @GetMapping("/reset")
    public ResponseEntity < ? > resetOpeningExpense ( @RequestParam Integer projectCoordinator, @RequestParam Integer month, @RequestParam Integer year ) {
        log.info( "Reset opening expense where project coordinator id is {}",projectCoordinator );
        openingExpenseService.resetOpeningExpense ( projectCoordinator,year,month );
        return new ResponseEntity <> ( HttpStatus.ACCEPTED );
    }

}
