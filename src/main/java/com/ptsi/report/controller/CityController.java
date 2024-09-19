package com.ptsi.report.controller;

import com.ptsi.report.model.request.CityRequest;
import com.ptsi.report.model.response.CityResponse;
import com.ptsi.report.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin( "*" )
@RequestMapping( "/api/v1/city" )
@RequiredArgsConstructor
@Slf4j
public class CityController {

    private final CityService cityService;

    @PutMapping
    public ResponseEntity<?> updateCity( @RequestBody CityRequest cityRequest ) {
        log.info( "update project coordinator where city id is {}", cityRequest.getCityId() );
        cityService.updateCity( cityRequest );
        return new ResponseEntity<>( HttpStatus.ACCEPTED );
    }

    @GetMapping
    public ResponseEntity< List< CityResponse > > fetchActiveCities(  ) {
        log.info( "Fetch al cities" );
        return new ResponseEntity<>( cityService.fetchActiveCities(),HttpStatus.OK );
    }


}
