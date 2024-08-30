package com.ptsi.report.service;

import com.ptsi.report.model.request.StaffRequest;
import com.ptsi.report.model.response.StaffResponse;

import java.util.List;

public interface StaffService {

    List< StaffResponse > fetchStaff();

    void updateStaff( StaffRequest staffRequest );
}
