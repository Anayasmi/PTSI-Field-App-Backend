package com.ptsi.report.repository;

import com.ptsi.report.constant.StaffType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class ExpenseReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public List< Map< String, Object > > fetchExpenseReport( String fromDate, String toDate, String staffId, StaffType staffType ) {
        try {

            // Construct the dynamic SQL
            String sql;
            if ( staffType == StaffType.SINGLE ) {

                sql = "DECLARE @ColumnList NVARCHAR(MAX);\n" +
                        "\n" +
                        "-- Generate dynamic column list for expense categories\n" +
                        "SELECT @ColumnList = STRING_AGG(\n" +
                        "    'MAX(CASE WHEN EX.ExpenseName = ''' + REPLACE(ExpenseName, '''', '''''') + ''' THEN SQ.ActualAmount ELSE 0 END) AS [' + LEFT(ExpenseName, 28) + ']',\n" +
                        "    ', '\n" +
                        ")\n" +
                        "FROM ExpenseCategory;\n" +
                        "\n" +
                        "DECLARE @Sql NVARCHAR(MAX);\n" +
                        "SET @Sql = '\n" +
                        "SELECT DISTINCT\n" +
                        "    DP.DPRId AS ''DPR No.'',\n" +
                        "    DP.Date AS ''Date'',\n" +
                        "    ISNULL(PF.PortionName, '''') AS ''Floor Level'',\n" +
                        "    ISNULL(P.ProjectNumber, '''') AS ''Project No.'',\n" +
                        "    ISNULL(P.ProjectName, '''') AS ''Project'',\n" +
                        "    ISNULL(CT.CityName, '''') AS ''City'',\n" +
                        "    CONCAT(S.FirstName, '' '', S.LastName) AS ''Field Staff Name'',\n" +
                        "    ISNULL(SDE.ActualExpense, 0) AS ''Actual Expense'',\n" +
                        "    DS.Status AS ''DPR Status'',\n" +
                        "    ' + @ColumnList + ',\n" +
                        "    DP.TotalActualExpense AS ''Day Total''\n" +
                        "FROM Staff S\n" +
                        "JOIN StaffExpenseTransaction ST ON S.StaffId = ST.StaffId \n" +
                        "    AND S.StaffId = ''" + staffId + "'' \n" +
                        "    AND ST.Date BETWEEN ''" + fromDate + "'' AND ''" + toDate + "''\n" +
                        "JOIN DailyProgressReport DP ON DP.FilledBy = S.StaffId AND DP.Date BETWEEN ''" + fromDate + "'' AND ''" + toDate + "''\n" +
                        "LEFT JOIN Project P ON P.ProjectId = DP.ProjectId\n" +
                        "LEFT JOIN City CT ON CT.CityId = P.CityId\n" +
                        "LEFT JOIN PortionFloor PF ON PF.PortionId = DP.PortionId\n" +
                        "LEFT JOIN (\n" +
                        "    SELECT DPRId, SUM(ActualAmount) AS ActualExpense\n" +
                        "    FROM DPRExpense\n" +
                        "    GROUP BY DPRId\n" +
                        ") SDE ON SDE.DPRId = DP.DPRId\n" +
                        "LEFT JOIN (\n" +
                        "    SELECT DPRId, ExpenseCategoryId, ActualAmount\n" +
                        "    FROM DPRExpense\n" +
                        ") SQ ON SQ.DPRId = DP.DPRId\n" +
                        "LEFT JOIN ExpenseCategory EX ON SQ.ExpenseCategoryId = EX.ExpenseCategoryId\n" +
                        "JOIN DPRStatus DS ON DS.StatusId = DP.DPRStatus\n" +
                        "GROUP BY \n" +
                        "    DP.DPRId,\n" +
                        "    DP.Date,\n" +
                        "    P.ProjectNumber,\n" +
                        "    P.ProjectName,\n" +
                        "    PF.PortionName,\n" +
                        "    CT.CityName,\n" +
                        "    CONCAT(S.FirstName, '' '', S.LastName),\n" +
                        "    DS.Status,\n" +
                        "    SDE.ActualExpense,\n" +
                        "    DP.TotalActualExpense\n" +
                        "ORDER BY DP.Date ASC;';\n" +
                        "\n" +
                        "-- Print the SQL for debugging\n" +
                        "PRINT @Sql;\n" +
                        "\n" +
                        "-- Execute the dynamic SQL\n" +
                        "EXEC sp_executesql @Sql;";
            } else {
                sql = "DECLARE @ColumnList NVARCHAR(MAX);\n" +
                        "\n" +
                        "-- Generate dynamic column list for expense categories\n" +
                        "SELECT @ColumnList = STRING_AGG(\n" +
                        "    'MAX(CASE WHEN EX.ExpenseName = ''' + REPLACE(ExpenseName, '''', '''''') + ''' THEN SQ.ActualAmount ELSE 0 END) AS [' + LEFT(ExpenseName, 28) + ']',\n" +
                        "    ', '\n" +
                        ")\n" +
                        "FROM ExpenseCategory;\n" +
                        "\n" +
                        "DECLARE @Sql NVARCHAR(MAX);\n" +
                        "SET @Sql = '\n" +
                        "SELECT DISTINCT\n" +
                        "    DP.DPRId AS ''DPR No.'',\n" +
                        "    DP.Date AS ''Date'',\n" +
                        "    ISNULL(PF.PortionName, '''') AS ''Floor Level'',\n" +
                        "    ISNULL(P.ProjectNumber, '''') AS ''Project No.'',\n" +
                        "    ISNULL(P.ProjectName, '''') AS ''Project'',\n" +
                        "    ISNULL(CT.CityName, '''') AS ''City'',\n" +
                        "    CONCAT(S.FirstName, '' '', S.LastName) AS ''Field Staff Name'',\n" +
                        "    ISNULL(SDE.ActualExpense, 0) AS ''Actual Expense'',\n" +
                        "    DS.Status AS ''DPR Status'',\n" +
                        "    ' + @ColumnList + ',\n" +
                        "    DP.TotalActualExpense AS ''Day Total''\n" +
                        "FROM Staff S\n" +
                        "JOIN StaffExpenseTransaction ST ON S.StaffId = ST.StaffId \n" +
                        "    AND S.StaffId IN (" + staffId + ") -- List of StaffIds here\n" +
                        "    AND ST.Date BETWEEN ''" + fromDate + "'' AND ''" + toDate + "''\n" +
                        "JOIN DailyProgressReport DP ON DP.FilledBy = S.StaffId AND DP.Date BETWEEN ''" + fromDate + "'' AND ''" + toDate + "''\n" +
                        "LEFT JOIN Project P ON P.ProjectId = DP.ProjectId\n" +
                        "LEFT JOIN City CT ON CT.CityId = P.CityId\n" +
                        "LEFT JOIN PortionFloor PF ON PF.PortionId = DP.PortionId\n" +
                        "LEFT JOIN (\n" +
                        "    SELECT DPRId, SUM(ActualAmount) AS ActualExpense\n" +
                        "    FROM DPRExpense\n" +
                        "    GROUP BY DPRId\n" +
                        ") SDE ON SDE.DPRId = DP.DPRId\n" +
                        "LEFT JOIN (\n" +
                        "    SELECT DPRId, ExpenseCategoryId, ActualAmount\n" +
                        "    FROM DPRExpense\n" +
                        ") SQ ON SQ.DPRId = DP.DPRId\n" +
                        "LEFT JOIN ExpenseCategory EX ON SQ.ExpenseCategoryId = EX.ExpenseCategoryId\n" +
                        "JOIN DPRStatus DS ON DS.StatusId = DP.DPRStatus\n" +
                        "GROUP BY \n" +
                        "    DP.DPRId,\n" +
                        "    DP.Date,\n" +
                        "    P.ProjectNumber,\n" +
                        "    P.ProjectName,\n" +
                        "    PF.PortionName,\n" +
                        "    CT.CityName,\n" +
                        "    CONCAT(S.FirstName, '' '', S.LastName),\n" +
                        "    DS.Status,\n" +
                        "    SDE.ActualExpense,\n" +
                        "    DP.TotalActualExpense\n" +
                        "ORDER BY DP.Date ASC;';\n" +
                        "\n" +
                        "-- Print the SQL for debugging\n" +
                        "PRINT @Sql;\n" +
                        "\n" +
                        "-- Execute the dynamic SQL\n" +
                        "EXEC sp_executesql @Sql;\n";
            }


            if ( staffId == null ) {
                return new ArrayList<>( );
            }


            // Execute the query
            return jdbcTemplate.queryForList( sql );
        }catch (Exception e) {
            e.printStackTrace( );
            return Collections.emptyList( );
        }
    }

    public List< Map< String, Object > > fetchExpenseSheet( LocalDate fromDate, LocalDate toDate, Integer staffId, Integer year, Integer month ) {

        try {

            // Construct the dynamic SQL
            String sql = "WITH DateRange AS (\n" +
                    "    SELECT CAST('"+fromDate+"' AS DATE) AS [Date]\n" +
                    "    UNION ALL\n" +
                    "    SELECT DATEADD(DAY, 1, [Date])\n" +
                    "    FROM DateRange\n" +
                    "    WHERE DATEADD(DAY, 1, [Date]) <= '"+toDate+"'\n" +
                    ")\n" +
                    "SELECT     \n" +
                    "    s.StaffId AS staffId,\n" +
                    "    CONCAT(LTRIM(RTRIM(s.FirstName)), ' ', LTRIM(RTRIM(s.LastName))) AS staffName,  \n" +
                    "    DR.[Date] AS [Date],\n" +
                    "    ISNULL(m.OpeningExpense, 0) AS openingExpense, \n" +
                    "    ISNULL(SUM(DP.TotalActualExpense), 0) AS totalActualExpense,\n" +
                    "    ISNULL(SUM(ARE.ApprovedAmount), 0) AS ApprovedAmount,\n" +
                    "\tISNULL(s.Tea,0) AS tea,\n" +
                    "\tISNULL(s.Telephone,0) AS telephone,\n" +
                    "\tISNULL(s.Petrol,0) AS petrol\n" +
                    "FROM \n" +
                    "    DateRange DR\n" +
                    "LEFT JOIN \n" +
                    "    Staff s ON s.StaffId = s.StaffId\n" +
                    "LEFT JOIN \n" +
                    "    MonthlyOpeningExpense m\n" +
                    "ON \n" +
                    "    s.StaffId = m.StaffId\n" +
                    "    AND m.Month = "+month+"\n" +
                    "    AND m.YEAR = "+year+"\n" +
                    "LEFT JOIN \n" +
                    "    (\n" +
                    "        SELECT \n" +
                    "            FilledBy,\n" +
                    "            Date,\n" +
                    "            SUM(TotalActualExpense) AS TotalActualExpense\n" +
                    "        FROM \n" +
                    "            DailyProgressReport\n" +
                    "        GROUP BY \n" +
                    "            FilledBy,\n" +
                    "            Date\n" +
                    "    ) DP\n" +
                    "ON \n" +
                    "    DP.FilledBy = s.StaffId\n" +
                    "    AND DP.Date = DR.[Date]\n" +
                    "LEFT JOIN \n" +
                    "    (\n" +
                    "        SELECT \n" +
                    "            CreatedBy,\n" +
                    "            ApprovedOn,\n" +
                    "            SUM(ApprovedAmount) AS ApprovedAmount\n" +
                    "        FROM \n" +
                    "            AdvanceRequestExpense\n" +
                    "        GROUP BY \n" +
                    "            CreatedBy,\n" +
                    "            ApprovedOn\n" +
                    "    ) ARE\n" +
                    "ON \n" +
                    "    ARE.CreatedBy = s.StaffId\n" +
                    "    AND ARE.ApprovedOn = DR.[Date]\n" +
                    "WHERE \n" +
                    "    s.ProjectCoordinator = "+staffId+" OR s.StaffId = "+staffId+"\n" +
                    "GROUP BY \n" +
                    "    s.StaffId,\n" +
                    "    s.FirstName,\n" +
                    "    s.LastName,\n" +
                    "    m.OpeningExpense,\n" +
                    "\ts.Tea,\n" +
                    "\ts.Telephone,\n" +
                    "\ts.Petrol,\n" +
                    "    DR.[Date]\n" +
                    "ORDER BY \n" +
                    "    CASE WHEN s.StaffId = "+staffId+" THEN 0 ELSE 1 END, -- Order by StaffId 52 first\n" +
                    "    DR.[Date] ASC;\n";
            if ( staffId == null ) {
                return new ArrayList<>( );
            }

            // Execute the query
            return jdbcTemplate.queryForList( sql );
        }catch (Exception e) {
            e.printStackTrace( );
            return Collections.emptyList( );
        }
    }
}