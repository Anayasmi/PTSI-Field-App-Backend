package com.ptsi.report.service;

import com.ptsi.report.model.request.ZoneRequest;
import com.ptsi.report.model.response.ZoneResponse;

import java.util.List;

public interface ZoneService {

    void updateZone( ZoneRequest zoneRequest );

    List< ZoneResponse > fetchAllZones();

}
