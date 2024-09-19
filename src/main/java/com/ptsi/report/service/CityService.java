package com.ptsi.report.service;

import com.ptsi.report.model.request.CityRequest;
import com.ptsi.report.model.response.CityResponse;

import java.util.List;

public interface CityService {

    void updateCity( CityRequest cityRequest );

    List< CityResponse > fetchActiveCities();

}
