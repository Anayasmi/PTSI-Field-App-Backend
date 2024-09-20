package com.ptsi.report.service;

import com.ptsi.report.model.request.AdvanceExpenseRequest;
import com.ptsi.report.model.response.AdvanceExpenseResponse;
import com.ptsi.report.model.response.CreationResponse;

import java.util.List;

public interface AdvanceExpenseService {

    CreationResponse updateAdvanceExpense( AdvanceExpenseRequest advanceExpenseRequest );

    List< AdvanceExpenseResponse > fetchAdvanceExpenses();

}
