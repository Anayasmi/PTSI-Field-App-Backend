package com.ptsi.report.repository;

import com.ptsi.report.entity.MonthlyOpeningExpense;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OpeningExpenseRepository extends JpaRepository< MonthlyOpeningExpense,Integer > {

    List<MonthlyOpeningExpense> findByProjectCoordinatorAndStaffId( Float projectCoordinator, Float staffId, Sort sort );

    List<MonthlyOpeningExpense> findByProjectCoordinatorAndYearAndMonthAndAdditionalStaff( Float projectCoordinator, Integer year, Integer month,Integer additionalStaff );

    List<MonthlyOpeningExpense> findByProjectCoordinatorAndStaffIdAndYearAndMonth( Float projectCoordinator,Float staffId, Integer year, Integer month );

    @Query(value = "SELECT s.StaffId AS staffId,\n" +
            "       CONCAT(TRIM(s.FirstName), ' ', TRIM(s.LastName)) AS staffName,\n" +
            "       ME.OpeningId AS openingId,\n" +
            "       ME.OpeningDate AS openingDate,\n" +
            "       ISNULL(ME.OpeningBalance, 0) AS openingBalance,\n" +
            "       ISNULL(ME.ClosingBalance, 0) AS closingBalance,\n" +
            "       ME.ClosingDate AS closingDate\n" +
            "FROM MonthlyOpeningExpense ME\n" +
            "LEFT JOIN Staff s ON s.StaffId=ME.StaffId\n" +
            "WHERE ME.OpeningDate BETWEEN :firstDayOfMonth AND :lastDayOfMonth\n" +
            "  AND ME.ProjectCoordinator=:staffId\n" +
            "  AND s.StaffId IS NOT NULL\n" +
            "ORDER BY CASE\n" +
            "             WHEN s.StaffId = :staffId THEN 1\n" +
            "             ELSE 2\n" +
            "         END;",nativeQuery = true)

    List < Map< String, Object > > findAllBYProjectCoordinator( Integer staffId, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth);

    @Query(value = "DECLARE @firstDayOfMonth DATETIME = :firstDayOfMonth;\n" +
            "DECLARE @lastDayOfMonth DATETIME = :lastDayOfMonth;\n" +
            "DECLARE @StaffId INT = :staffId;\n" +
            "\n" +
            "SELECT s.StaffId AS staffId,\n" +
            "       CONCAT(TRIM(s.FirstName), ' ', TRIM(s.LastName)) AS staffName,\n" +
            "       ME.OpeningId AS openingId,\n" +
            "       ME.OpeningDate AS openingDate,\n" +
            "       ME.ClosingDate AS closingDate,\n" +
            "       ISNULL(ME.OpeningBalance, 0) AS openingBalance,\n" +
            "       ISNULL(ME.ClosingBalance, 0) AS closingBalance,\n" +
            "       CASE \n" +
            "           WHEN ME.OpeningId IS NULL THEN 0 \n" +
            "           ELSE 1 \n" +
            "       END AS checkField -- 'check' is a keyword in some databases, so renamed to 'checkField'\n" +
            "FROM City c\n" +
            "LEFT JOIN Staff s ON s.CityId = c.CityId\n" +
            "LEFT JOIN Users u ON u.FirstName = s.FirstName\n" +
            "                 AND u.LastName = s.LastName\n" +
            "LEFT JOIN MonthlyOpeningExpense ME ON ME.StaffId = s.StaffId\n" +
            "                                  AND ME.OpeningDate BETWEEN @firstDayOfMonth AND @lastDayOfMonth\n" +
            "WHERE c.ProjectCoordinator = @StaffId\n" +
            "  AND u.IsLoginActive <> 0\n" +
            "  AND s.FirstName NOT LIKE '%Test%'\n" +
            "  AND s.FirstName NOT LIKE '%Admin%'\n" +
            "  AND s.StaffId IS NOT NULL\n" +
            "ORDER BY CASE\n" +
            "           WHEN s.StaffId = @StaffId THEN 1\n" +
            "           ELSE 2\n" +
            "         END;\n",nativeQuery = true)
    List < Map < String, Object > > findStaffBYProjectCoordinator( Integer staffId, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth);

    @Query(value = "\n" +
            "SELECT s.StaffId AS staffId,\n" +
            "       CONCAT(TRIM(s.FirstName), ' ', TRIM(s.LastName)) AS staffName,\n" +
            "       ME.OpeningId AS openingId,\n" +
            "       ME.OpeningDate AS openingDate,\n" +
            "       ME.ClosingDate AS closingDate,\n" +
            "       ISNULL(ME.OpeningBalance, 0) AS openingBalance,\n" +
            "       ISNULL(ME.ClosingBalance, 0) AS closingBalance\n" +
            "FROM MonthlyOpeningExpense ME\n" +
            "LEFT JOIN Staff s ON s.StaffId=ME.StaffId\n" +
            "AND ME.OpeningDate BETWEEN :firstDayOfMonth AND :lastDayOfMonth\n" +
            "WHERE ME.ProjectCoordinator=:staffId\n" +
            "  AND ME.AdditionalStaff =1\n" +
            "  AND s.StaffId IS NOT NULL\n" +
            "ORDER BY CASE\n" +
            "             WHEN s.StaffId = :staffId THEN 1\n" +
            "             ELSE 2\n" +
            "         END;",nativeQuery = true)
    List < Map < String, Object > > findAdditionalStaffBYProjectCoordinator( Integer staffId, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth );


    @Query(value = "DECLARE @FromDate DATE = CAST(:firstDayOfMonth AS DATE);\n" +
            "\n" +
            "DECLARE @ToDate DATE = CAST(:lastDayOfMonth AS DATE);\n" +
            "\n" +
            "DECLARE @ProjectCoordinatorId INT = :staffId;\n" +
            "\n" +
            "WITH DateRange AS\n" +
            "  (SELECT CAST(@FromDate AS DATE) AS [Date]\n" +
            "   UNION ALL SELECT DATEADD(DAY, 1, [Date])\n" +
            "   FROM DateRange\n" +
            "   WHERE DATEADD(DAY, 1, [Date]) <= @ToDate)\n" +
            "SELECT s.StaffId AS staffId,\n" +
            "       CONCAT(TRIM(s.FirstName), ' ', TRIM(s.LastName)) AS staffName,\n" +
            "       DR.[Date] AS date,\n" +
            "       ISNULL(SUM(DP.TotalActualExpense), 0) AS totalActualExpense,\n" +
            "       ISNULL(SUM(TRY_CAST(DP.amountCash AS float)), 0) AS amountCash,\n" +
            "       CASE\n" +
            "           WHEN s.StaffId = @ProjectCoordinatorId\n" +
            "                AND ARE.ApprovedBy IN (1,\n" +
            "                                       7) THEN 0\n" +
            "           ELSE ISNULL(SUM(ARE.ApprovedAmount), 0)\n" +
            "       END AS approvedAmount\n" +
            "FROM DateRange DR\n" +
            "LEFT JOIN Staff s ON s.CityId IS NOT NULL -- Adjust if needed\n" +
            "LEFT JOIN City c ON s.CityId = c.CityId\n" +
            "LEFT JOIN Users u ON u.FirstName = s.FirstName\n" +
            "AND u.LastName = s.LastName\n" +
            "LEFT JOIN\n" +
            "  (SELECT FilledBy, Date, SUM(CAST(TotalActualExpense AS float)) AS TotalActualExpense,\n" +
            "                          ISNULL(SUM(TRY_CAST(Request_Amount_Cash AS float)), 0) AS amountCash\n" +
            "   FROM DailyProgressReport\n" +
            "   GROUP BY FilledBy, Date) DP ON DP.FilledBy = s.StaffId\n" +
            "AND DP.Date = DR.[Date]\n" +
            "LEFT JOIN\n" +
            "  (SELECT CreatedBy,\n" +
            "          ApprovedOn,\n" +
            "          SUM(CAST(ApprovedAmount AS float)) AS approvedAmount,\n" +
            "          ApprovedBy\n" +
            "   FROM AdvanceRequestExpense\n" +
            "   WHERE ApprovedBy NOT IN (1,\n" +
            "                            7)\n" +
            "   GROUP BY CreatedBy,\n" +
            "            ApprovedOn,\n" +
            "            ApprovedBy) ARE ON ARE.CreatedBy = s.StaffId\n" +
            "AND ARE.ApprovedOn = DR.[Date]\n" +
            "WHERE c.ProjectCoordinator = @ProjectCoordinatorId\n" +
            "  AND u.IsLoginActive <> 0\n" +
            "  AND s.FirstName NOT LIKE '%Test%'\n" +
            "  AND s.FirstName NOT LIKE '%Admin%'\n" +
            "GROUP BY s.StaffId,\n" +
            "         s.FirstName,\n" +
            "         s.LastName,\n" +
            "         DR.[Date],\n" +
            "         ARE.ApprovedBy\n" +
            "ORDER BY s.StaffId OPTION (MAXRECURSION 0);",nativeQuery = true)
    List < Map < String, Object > > findExpenseBYProjectCoordinator( Integer staffId, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth );

    @Query(value = "DECLARE @FromDate DATE = CAST(:firstDayOfMonth AS DATE);\n" +
            "DECLARE @ToDate DATE = CAST(:lastDayOfMonth AS DATE);\n" +
            "DECLARE @StaffId INT = :staffId;\n" +
            "DECLARE @ProjectCoordinator INT = :projectCoordinator;\n" +
            "\n" +
            "WITH DateRange AS\n" +
            "(SELECT CAST(@FromDate AS DATE) AS [Date]\n" +
            "UNION ALL SELECT DATEADD(DAY, 1, [Date])\n" +
            "FROM DateRange\n" +
            "WHERE DATEADD(DAY, 1, [Date]) <= @ToDate)\n" +
            "\n" +
            "SELECT s.StaffId AS staffId,\n" +
            "CONCAT(TRIM(s.FirstName), ' ', TRIM(s.LastName)) AS staffName,\n" +
            "DR.[Date] AS date,\n" +
            "ISNULL(SUM(DP.TotalActualExpense), 0) AS totalActualExpense,\n" +
            "ISNULL(SUM(TRY_CAST(DP.amountCash AS float)), 0) AS amountCash,\n" +
            "CASE\n" +
            "WHEN s.StaffId = @ProjectCoordinator\n" +
            "AND ARE.ApprovedBy IN (1, 7) THEN 0\n" +
            "ELSE ISNULL(SUM(ARE.ApprovedAmount), 0)\n" +
            "END AS approvedAmount\n" +
            "FROM DateRange DR\n" +
            "LEFT JOIN Staff s ON s.StaffId=@StaffId\n" +
            "LEFT JOIN\n" +
            "(SELECT FilledBy, Date, SUM(CAST(TotalActualExpense AS float)) AS TotalActualExpense,\n" +
            "ISNULL(SUM(TRY_CAST(Request_Amount_Cash AS float)), 0) AS amountCash\n" +
            "FROM DailyProgressReport\n" +
            "GROUP BY FilledBy, Date) DP ON DP.FilledBy = s.StaffId\n" +
            "AND DP.Date = DR.[Date]\n" +
            "LEFT JOIN\n" +
            "(SELECT CreatedBy,\n" +
            "ApprovedOn,\n" +
            "SUM(CAST(ApprovedAmount AS float)) AS approvedAmount,\n" +
            "ApprovedBy\n" +
            "FROM AdvanceRequestExpense\n" +
            "WHERE ApprovedBy NOT IN (1,\n" +
            "7)\n" +
            "GROUP BY CreatedBy,\n" +
            "ApprovedOn,\n" +
            "ApprovedBy) ARE ON ARE.CreatedBy = s.StaffId\n" +
            "AND ARE.ApprovedOn = DR.[Date]\n" +
            "GROUP BY s.StaffId,\n" +
            "s.FirstName,\n" +
            "s.LastName,\n" +
            "DR.[Date],\n" +
            "ARE.ApprovedBy\n" +
            "ORDER BY s.StaffId OPTION (MAXRECURSION 0);",nativeQuery = true)
    List < Map < String, Object > > findExpenseBYProjectCoordinatorAndStaff( Float projectCoordinator,Float staffId, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth );


    List<MonthlyOpeningExpense> findByMonthAndYearAndProjectCoordinatorAndAdditionalStaff( Integer month,Integer year,Float projectCoordinator,Integer additionalStaff );
}
