package com.ptsi.report.service;

import com.ptsi.report.model.request.OpeningExpenseRequest;
import com.ptsi.report.model.response.OpeningExpenseResponse;

import java.util.List;

public interface OpeningExpenseService {

    void saveOrUpdateOpeningExpense( OpeningExpenseRequest openingExpenseRequest );

    List< OpeningExpenseResponse > fetchByProjectCoordinator(Integer staffId,Integer month,Integer year);
}
