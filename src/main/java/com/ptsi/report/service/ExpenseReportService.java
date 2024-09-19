package com.ptsi.report.service;

import com.ptsi.report.constant.StaffType;
import com.ptsi.report.model.response.ExpenseSheetResponse;

import java.util.List;
import java.util.Map;

public interface ExpenseReportService {
    List < Map <String, Object> > fetchExpenseReport( Integer year,Integer month, Float staffId, StaffType staffType );
    List < ExpenseSheetResponse > fetchExpenseSheet ( Integer year,Integer month,Integer staffId,Double openingBalance );
    List <ExpenseSheetResponse> fetchByProjectCoordinator (Integer year,Integer month,Integer staffId );
}
