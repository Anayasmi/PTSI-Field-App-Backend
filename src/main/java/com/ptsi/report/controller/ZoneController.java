package com.ptsi.report.controller;

import com.ptsi.report.model.request.ZoneRequest;
import com.ptsi.report.model.response.ZoneResponse;
import com.ptsi.report.service.ZoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin( "*" )
@RequestMapping( "/api/v1/zone" )
@RequiredArgsConstructor
@Slf4j
public class ZoneController {

    private final ZoneService zoneService;

    @PutMapping
    public ResponseEntity<?> updateZone( @RequestBody ZoneRequest zoneRequest ) {
        log.info( "update project coordinator where zone id is {}", zoneRequest.getZoneId() );
        zoneService.updateZone( zoneRequest );
        return new ResponseEntity<>( HttpStatus.ACCEPTED );
    }

    @GetMapping
    public ResponseEntity< List< ZoneResponse > > fetchAllZones(  ) {
        log.info( "Fetch all zones" );
        return new ResponseEntity<>( zoneService.fetchAllZones(),HttpStatus.OK );
    }
}
