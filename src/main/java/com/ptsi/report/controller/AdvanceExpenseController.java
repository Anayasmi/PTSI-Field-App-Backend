package com.ptsi.report.controller;

import com.ptsi.report.model.request.AdvanceExpenseRequest;
import com.ptsi.report.model.response.AdvanceExpenseResponse;
import com.ptsi.report.model.response.CreationResponse;
import com.ptsi.report.service.AdvanceExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin( "*" )
@RequestMapping( "/api/v1/advance" )
@RequiredArgsConstructor
@Slf4j
public class AdvanceExpenseController {

    private final AdvanceExpenseService advanceExpenseService;

    @PutMapping
    public ResponseEntity< CreationResponse > updateAdvanceExpense( @RequestBody AdvanceExpenseRequest advanceExpenseRequest ) {
        log.info( "update advance expense  where advance request  id is {}", advanceExpenseRequest.getAdvanceRequestId() );

        return new ResponseEntity<>( advanceExpenseService.updateAdvanceExpense( advanceExpenseRequest ),HttpStatus.ACCEPTED );
    }

    @GetMapping
    public ResponseEntity< List< AdvanceExpenseResponse > > fetchAdvanceExpenses(  ) {
        log.info( "Fetch all advance expenses" );
        return new ResponseEntity<>( advanceExpenseService.fetchAdvanceExpenses(),HttpStatus.OK );
    }


}
