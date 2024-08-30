package com.ptsi.report.repository;

import com.ptsi.report.entity.MonthlyOpeningExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface OpeningExpenseRepository extends JpaRepository< MonthlyOpeningExpense,Integer > {

    MonthlyOpeningExpense findByYearAndMonthAndStaffId( Integer year, Integer month,Float staffId);

    @Query(value = "SELECT \n" +
            "    ISNULL(m.ExpenseId, 0) AS expenseId,\n" +
            "    s.StaffId AS staffId, \n" +
            "    CONCAT(s.FirstName, ' ', s.LastName) AS staffName, \n" +
            "    ISNULL(m.OpeningExpense, 0) AS openingExpense\n" +
            "FROM \n" +
            "    Staff s\n" +
            "LEFT JOIN \n" +
            "    MonthlyOpeningExpense m\n" +
            "ON \n" +
            "    s.StaffId = m.StaffId\n" +
            "    AND m.Month = :month \n" +
            "    AND m.YEAR = :year\n" +
            "WHERE \n" +
            "    s.ProjectCoordinator = :staffId OR s.StaffId = :staffId\n" +
            "ORDER BY \n" +
            "    CASE \n" +
            "        WHEN s.StaffId = :staffId THEN 1\n" +
            "        ELSE 2\n" +
            "    END;",nativeQuery = true)
    List < Map < String, Object > > findByStaff( Integer staffId,Integer month,Integer year);


}
