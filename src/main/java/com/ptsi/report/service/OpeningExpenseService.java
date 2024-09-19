package com.ptsi.report.service;

import com.ptsi.report.constant.StaffCategory;
import com.ptsi.report.model.request.OpeningExpenseRequest;
import com.ptsi.report.model.response.OpeningExpenseResponses;

import java.util.List;

public interface OpeningExpenseService {

    void saveOrUpdateOpeningExpense( OpeningExpenseRequest openingExpenseRequest );

    List< OpeningExpenseResponses >  fetchByProjectCoordinator( Integer staffId, Integer month, Integer year, StaffCategory staffCategory);

    void resetOpeningExpense ( Integer projectCoordinator,Integer year, Integer month );
}
