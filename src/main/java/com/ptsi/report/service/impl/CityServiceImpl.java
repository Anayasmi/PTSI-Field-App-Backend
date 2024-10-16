package com.ptsi.report.service.impl;

import com.ptsi.report.entity.City;
import com.ptsi.report.model.request.CityRequest;
import com.ptsi.report.model.response.CityResponse;
import com.ptsi.report.repository.CityRepository;
import com.ptsi.report.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
        city.setUpdatedDate( LocalDateTime.now() );
        cityRepository.save( city );
    }

    @Override
    public List< CityResponse > fetchActiveCities() {

        List< Map<String,Object> > zoneMasters = cityRepository.getCities( );
        return zoneMasters.stream( ).map( e -> CityResponse.builder( )
                .cityName(  getStringValue(  e.get( "cityName" ) ))
                .cityId( getDoubleValue( e.get( "cityId" ) ))
                .build( ) ).collect( Collectors.toList( ) );
    }

    Double getDoubleValue(Object object){
        if(object instanceof Double) {
           return  ( Double ) object;
        }
        if ( object instanceof Float ){
            return Double.valueOf( ( Float ) object);
        }
        return 0.0;
    }


    String getStringValue(Object object){
        if(object instanceof String){
            return  ( String ) object ;
        }
        return "";
    }


}
