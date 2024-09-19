package com.ptsi.report.service;

import com.ptsi.report.constant.StaffCategory;
import com.ptsi.report.model.request.MonthlyExpenseRequest;
import com.ptsi.report.model.response.MonthlyExpenseResponse;

import java.util.List;

public interface MonthlyExpenseService {

    void saveOrUpdateMonthlyExpense( MonthlyExpenseRequest monthlyExpenseRequest );

    List< MonthlyExpenseResponse > fetchByProjectCoordinator( Integer staffId, Integer year, Integer month, StaffCategory staffCategory );
}
