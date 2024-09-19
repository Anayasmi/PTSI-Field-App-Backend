package com.ptsi.report.service.impl;

import com.ptsi.report.entity.City;
import com.ptsi.report.model.request.CityRequest;
import com.ptsi.report.model.response.CityResponse;
import com.ptsi.report.repository.CityRepository;
import com.ptsi.report.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    @Override
    public void updateCity( CityRequest cityRequest ) {

        City city = cityRepository.findById( cityRequest.getCityId() ).orElseThrow();
        city.setProjectCoordinator( cityRequest.getProjectCoordinator() );
        city.setUpdatedDate( LocalDateTime.now() );
        cityRepository.save( city );
    }

    @Override
    public List< CityResponse > fetchActiveCities() {

        List< Map<String,Object> > zoneMasters = cityRepository.getCities( );
        return zoneMasters.stream( ).map( e -> CityResponse.builder( )
                .staffName( ( String ) e.get( "staffName" ) )
                .projectCoordinator( ( Double ) e.get( "projectCoordinator" ) )
                .cityName(  ( String ) e.get( "cityName" ) )
                .cityId( ( Double ) e.get( "cityId" ) )
                .build( ) ).collect( Collectors.toList( ) );
    }


}
