package com.ptsi.report.repository;

import com.ptsi.report.entity.AdvanceRequestExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvanceExpenseRepository extends JpaRepository< AdvanceRequestExpense,Float > {
}
