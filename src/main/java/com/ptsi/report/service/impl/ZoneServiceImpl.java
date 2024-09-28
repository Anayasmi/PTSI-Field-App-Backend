package com.ptsi.report.service.impl;

import com.ptsi.report.entity.Staff;
import com.ptsi.report.entity.ZoneMaster;
import com.ptsi.report.model.request.ZoneRequest;
import com.ptsi.report.model.response.StaffValue;
import com.ptsi.report.model.response.ZoneResponse;
import com.ptsi.report.repository.StaffRepository;
import com.ptsi.report.repository.ZoneRepository;
import com.ptsi.report.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;
    private final StaffRepository staffRepository;

    @Override
    public void updateZone( ZoneRequest zoneRequest ) {

        ZoneMaster zoneMaster = zoneRepository.findById( zoneRequest.getZoneId( ) ).orElseThrow( );

        List < String > projectCoordinators = new ArrayList <>( );
        if ( zoneMaster.getProjectCoordinator( ) != null ) {
            projectCoordinators = Arrays.stream( zoneMaster.getProjectCoordinator( ).split( "," ) )
                    .collect( Collectors.toList( ) );
            projectCoordinators = projectCoordinators.stream( ).distinct( ).collect( Collectors.toList( ) );
        }
        if ( zoneRequest.getIsUpdate( ) ) {
            projectCoordinators.add( zoneRequest.getProjectCoordinator( ) );
            projectCoordinators = projectCoordinators.stream( ).distinct( ).collect( Collectors.toList( ) );
        }
        if ( zoneRequest.getIsDeleted( ) ) {
            projectCoordinators.remove( zoneRequest.getProjectCoordinator( ) );
        }

        zoneMaster.setUpdatedDate( LocalDateTime.now( ) );
        zoneMaster.setProjectCoordinator( String.join( "," , projectCoordinators ) );
        zoneRepository.save( zoneMaster );

    }

    @Override
    public List < ZoneResponse > fetchAllZones( ) {
        List < ZoneMaster > zoneMasters = zoneRepository.findByIsDeleted( 0.0F );

        List < Float > projectCoordinators = zoneMasters.stream( )
                .flatMap( zoneMaster -> ( zoneMaster.getProjectCoordinator( ) == null )? Stream.empty( ) : Arrays.stream( zoneMaster.getProjectCoordinator( ).split( "," ) ) )
                .map( coordinator -> {
                    try {
                        return Float.parseFloat( coordinator );
                    } catch ( NumberFormatException e ) {
                        System.err.println( "Invalid float: " + coordinator );
                        return null;
                    }
                } )
                .filter( Objects :: nonNull )
                .distinct( )
                .toList( );

        List < Staff > staffList = staffRepository.findByStaffIdIn( projectCoordinators );

        return zoneMasters.stream( )
                .map( zoneMaster -> {
                    String projectCoordinator = zoneMaster.getProjectCoordinator( );

                    List<Float> results = (projectCoordinator == null || projectCoordinator.isEmpty())
                            ? new ArrayList<>()
                            : Arrays.stream(projectCoordinator.split(","))
                            .map(coordinator -> {
                                try {
                                    return Float.parseFloat(coordinator);
                                } catch (NumberFormatException e) {
                                    return null; // Handle or log the error
                                }
                            })
                            .filter(Objects::nonNull) // Filter out null values
                            .toList();


                    List < StaffValue > staffValues = results.stream( )
                            .map( e -> getStaff( staffList , e ) )
                            .toList( );

                    return ZoneResponse.builder( )
                            .zoneId( zoneMaster.getZoneId( ) )
                            .zoneName( zoneMaster.getZoneName( ) )
                            .projectCoordinators( staffValues )
                            .build( );
                } )
                .toList( );
    }

    StaffValue getStaff( List < Staff > staffList , Float staffId ) {
        return staffList.stream( )
                .filter( e -> Objects.equals( e.getStaffId( ) , staffId ) )
                .findFirst( )
                .map( staff -> new StaffValue(
                        Double.valueOf( staffId ) ,
                        staff.getFirstName( ).trim( ) + " " + staff.getLastName( ).trim( )
                ) )
                .orElse( new StaffValue( ) );
    }
}
