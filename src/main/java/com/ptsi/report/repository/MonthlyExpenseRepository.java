package com.ptsi.report.repository;

import com.ptsi.report.entity.MonthlyExpense;
import com.ptsi.report.entity.MonthlyOpeningExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MonthlyExpenseRepository extends JpaRepository< MonthlyExpense,Integer > {

    MonthlyExpense findByYearAndMonthAndStaffId( Integer year, Integer month, Float staffId);

    @Query(value = "\n" +
            "SELECT s.StaffId AS staffId,\n" +
            "       CONCAT(TRIM(s.FirstName), ' ', TRIM(s.LastName)) AS staffName,\n" +
            "       ISNULL(ME.Tea, 0) AS tea,\n" +
            "       ISNULL(ME.Telephone, 0) AS telephone,\n" +
            "       ISNULL(ME.Petrol, 0) AS petrol\n" +
            "FROM MonthlyExpense ME\n" +
            "LEFT JOIN Staff s ON s.StaffId=ME.StaffId\n" +
            "WHERE ME.Month=:month\n" +
            "  AND ME.Year=:year\n" +
            "  AND ME.ProjectCoordinator=:staffId\n" +
            "  AND s.StaffId IS NOT NULL\n" +
            "ORDER BY CASE\n" +
            "             WHEN s.StaffId = :staffId THEN 1\n" +
            "             ELSE 2\n" +
            "         END;",nativeQuery = true)

    List < Map < String, Object > > findAllBYProjectCoordinator( Integer staffId, Integer year, Integer month);


    @Query(value = "SELECT \n" +
            "    s.StaffId AS staffId,\n" +
            "    CONCAT(TRIM(s.FirstName), ' ', TRIM(s.LastName)) AS staffName,  \n" +
            "    ISNULL(ME.Tea, 0) AS tea,  \n" +
            "    ISNULL(ME.Telephone, 0) AS telephone,  \n" +
            "    ISNULL(ME.Petrol, 0) AS petrol  \n" +
            "FROM Staff s\n" +
            "LEFT JOIN Users u ON u.FirstName = s.FirstName  \n" +
            "AND u.LastName = s.LastName\n" +
            "LEFT JOIN MonthlyExpense ME ON ME.StaffId = s.StaffId  \n" +
            "AND ME.Month = :month  \n" +
            "AND ME.Year = :year\n" +
            "AND ME.AdditionalStaff=0 \n"+
            "WHERE s.ProjectCoordinator = :staffId  \n" +
            "  AND u.IsLoginActive <> 0  \n" +
            "  AND s.FirstName NOT LIKE '%Test%'  \n" +
            "  AND s.FirstName NOT LIKE '%Admin%'  \n" +
            "  AND s.Active <> 0 \n" +
            "  AND s.StaffId IS NOT NULL  \n" +
            "GROUP BY \n" +
            "    s.StaffId, \n" +
            "    s.FirstName, \n" +
            "    s.LastName,  \n" +
            "    ME.Tea,  \n" +
            "    ME.Telephone,  \n" +
            "    ME.Petrol  \n" +
            "ORDER BY \n" +
            "    CASE\n" +
            "        WHEN s.StaffId = :staffId THEN 1  \n" +
            "        ELSE 2  \n" +
            "    END;\n",nativeQuery = true)
    List < Map < String, Object > > findStaffBYProjectCoordinator( Integer staffId, Integer year, Integer month);

    @Query(value = "SELECT s.StaffId AS staffId,\n" +
            "       CONCAT(TRIM(s.FirstName), ' ', TRIM(s.LastName)) AS staffName,\n" +
            "       ISNULL(ME.Tea, 0) AS tea,\n" +
            "       ISNULL(ME.Telephone, 0) AS telephone,\n" +
            "       ISNULL(ME.Petrol, 0) AS petrol\n" +
            "FROM MonthlyExpense ME\n" +
            "LEFT JOIN Staff s ON s.StaffId=ME.StaffId\n" +
            "AND ME.Month=:month\n" +
            "AND ME.Year=:year\n" +
            "WHERE ME.ProjectCoordinator=:staffId\n" +
            "  AND ME.AdditionalStaff =1\n" +
            "  AND s.StaffId IS NOT NULL\n" +
            "GROUP BY s.StaffId,\n" +
            "         s.FirstName,\n" +
            "         s.lastName,\n" +
            "         ME.Tea,\n" +
            "         ME.Telephone,\n" +
            "         ME.Petrol\n" +
            "ORDER BY CASE\n" +
            "             WHEN s.StaffId = :staffId THEN 1\n" +
            "             ELSE 2\n" +
            "         END;",nativeQuery = true)
    List < Map < String, Object > > findAdditionalStaffBYProjectCoordinator( Integer staffId, Integer year, Integer month);


}
