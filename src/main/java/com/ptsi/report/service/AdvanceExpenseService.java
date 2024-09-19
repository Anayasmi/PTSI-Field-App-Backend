package com.ptsi.report.service;

import com.ptsi.report.model.request.AdvanceExpenseRequest;
import com.ptsi.report.model.response.AdvanceExpenseResponse;

import java.util.List;

public interface AdvanceExpenseService {

    void updateAdvanceExpense( AdvanceExpenseRequest advanceExpenseRequest );

    List< AdvanceExpenseResponse > fetchAdvanceExpenses();

}
