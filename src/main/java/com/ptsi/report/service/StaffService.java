package com.ptsi.report.service;

import com.ptsi.report.model.request.StaffRequest;
import com.ptsi.report.model.response.CommonResponse;
import com.ptsi.report.model.response.StaffResponse;
import com.ptsi.report.model.response.StaffValue;

import java.util.List;
import java.util.Map;

public interface StaffService {

    List< StaffResponse > fetchStaff(Float projectCoordinator);

    void updateStaff( StaffRequest staffRequest );

    List< StaffValue > fetchAllProCo();

    List< StaffValue > findAllStaffNotInProjectCoordinator(Float staffId);

    Map <String,List< CommonResponse >>  fetchAllRequiredData();
}
