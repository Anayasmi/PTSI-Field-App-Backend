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

                sql = "DECLARE @FromDate DATETIME = '"+fromDate+"';\n" +
                        "\n" +
                        "DECLARE @ToDate DATETIME = '"+toDate+"';\n" +
                        "\n" +
                        "DECLARE @StaffId INT = "+staffId+";\n" +
                        "\n" +
                        "DECLARE @ColumnList NVARCHAR(MAX);\n" +
                        "\n" +
                        "-- Generate dynamic column list for expense categories\n" +
                        "\n" +
                        "SELECT @ColumnList = STRING_AGG('MAX(CASE WHEN EX.ExpenseName = ''' + REPLACE(ExpenseName, '''', '''''') + ''' THEN SQ.ActualAmount ELSE 0 END) AS [' + LEFT(ExpenseName, 28) + ']', ', ')\n" +
                        "FROM ExpenseCategory;\n" +
                        "\n" +
                        "DECLARE @Sql NVARCHAR(MAX);\n" +
                        "\n" +
                        "\n" +
                        "SET @Sql = 'SELECT DISTINCT\n" +
                        "\t\t\tDP.DPRId AS ''DPR No.'',\n" +
                        "\t\t\tDP.Date AS ''Date'',\n" +
                        "\t\t\tISNULL(PF.PortionName, '''') AS ''Floor Level'',\n" +
                        "\t\t\tISNULL(P.ProjectNumber, '''') AS ''Project No.'',\n" +
                        "\t\t\tISNULL(P.ProjectName, '''') AS ''Project'',\n" +
                        "\t\t\tISNULL(CT.CityName, '''') AS ''City'',\n" +
                        "\t\t\tCONCAT(pc.FirstName, '' '', pc.LastName) AS ''Project Coordinator'',\n" +
                        "\t\t\tCONCAT(S.FirstName, '' '', S.LastName) AS ''Field Staff Name'',\n" +
                        "\t\t\tISNULL(SDE.ActualExpense, 0) AS ''Actual Expense'',\n" +
                        "\t\t\tDS.Status AS ''DPR Status'',\n" +
                        "\t\t\t' + @ColumnList + ',\n" +
                        "\t\t\tDP.TotalActualExpense AS ''Day Total''\n" +
                        "\t\t\tFROM Staff S\n" +
                        "\t\t\tLEFT JOIN Staff pc ON s.ProjectCoordinator = pc.StaffId\n" +
                        "\t\t\tJOIN StaffExpenseTransaction ST ON S.StaffId = ST.StaffId \n" +
                        "\t\t\tAND S.StaffId = @StaffId \n" +
                        "\t\t\tJOIN DailyProgressReport DP ON DP.FilledBy = S.StaffId \n" +
                        "\t\t\tAND DP.Date BETWEEN @FromDate AND @ToDate LEFT JOIN Project P ON P.ProjectId = DP.ProjectId\n" +
                        "\t\t\tLEFT JOIN City CT ON CT.CityId = P.CityId\n" +
                        "\t\t\tLEFT JOIN PortionFloor PF ON PF.PortionId = DP.PortionId\n" +
                        "\t\t\tLEFT JOIN (\n" +
                        "\t\t\t\tSELECT DPRId, SUM(ActualAmount) AS ActualExpense\n" +
                        "\t\t\t\tFROM DPRExpense\n" +
                        "\t\t\t\tGROUP BY DPRId\n" +
                        "\t\t\t\t) SDE ON SDE.DPRId = DP.DPRId\n" +
                        "\t\t\tLEFT JOIN (\n" +
                        "\t\t\t\tSELECT DPRId, ExpenseCategoryId, ActualAmount\n" +
                        "\t\t\t\tFROM DPRExpense\n" +
                        "\t\t\t\t) SQ ON SQ.DPRId = DP.DPRId\n" +
                        "\t\t\tLEFT JOIN ExpenseCategory EX ON SQ.ExpenseCategoryId = EX.ExpenseCategoryId\n" +
                        "\t\t\tJOIN DPRStatus DS ON DS.StatusId = DP.DPRStatus\n" +
                        "\t\t\tGROUP BY\n" +
                        "\t\t\t\tDP.DPRId, \n" +
                        "\t\t\t\tDP.Date,\n" +
                        "\t\t\t\tP.ProjectNumber, \n" +
                        "\t\t\t\tP.ProjectName,\n" +
                        "\t\t\t\tPF.PortionName,\n" +
                        "\t\t\t\tCT.CityName,\n" +
                        "\t\t\t\tCONCAT(pc.FirstName, '' '', pc.LastName),\n" +
                        "\t\t\t\tCONCAT(S.FirstName, '' '', S.LastName),\n" +
                        "\t\t\t\tDS.Status,\n" +
                        "\t\t\t\tSDE.ActualExpense,\n" +
                        "\t\t\t\tDP.TotalActualExpense\n" +
                        "\t\t\t\tORDER BY DP.Date ASC;';\n" +
                        "\n" +
                        "PRINT @Sql;\n" +
                        "\n" +
                        "EXEC sp_executesql @Sql,\n" +
                        "     N'@FromDate DATETIME, @ToDate DATETIME, @StaffId INT',\n" +
                        "      @FromDate,\n" +
                        "      @ToDate,\n" +
                        "      @StaffId;";
            } else {
                sql="DECLARE @FromDate DATETIME = '"+fromDate+"';\n" +
                        "\n" +
                        "DECLARE @ToDate DATETIME = '"+toDate+"';\n" +
                        "\n" +
                        "DECLARE @StaffId INT = "+staffId+";\n" +
                        "\n" +
                        "DECLARE @ColumnList NVARCHAR(MAX);\n" +
                        "\n" +
                        "-- Generate dynamic column list for expense categories\n" +
                        "\n" +
                        "SELECT @ColumnList = STRING_AGG('MAX(CASE WHEN EX.ExpenseName = ''' + REPLACE(ExpenseName, '''', '''''') + ''' THEN SQ.ActualAmount ELSE 0 END) AS [' + LEFT(ExpenseName, 28) + ']', ', ')\n" +
                        "FROM ExpenseCategory;\n" +
                        "\n" +
                        "DECLARE @Sql NVARCHAR(MAX);\n" +
                        "\n" +
                        "\n" +
                        "SET @Sql = '\n" +
                        "SELECT DISTINCT\n" +
                        "    DP.DPRId AS ''DPR No.'',\n" +
                        "    DP.Date AS ''Date'',\n" +
                        "    ISNULL(PF.PortionName,'''') AS ''Floor Level'',\n" +
                        "    ISNULL(P.ProjectNumber,'''') AS ''Project No.'',\n" +
                        "    ISNULL(P.ProjectName,'''') AS ''Project'',\n" +
                        "    ISNULL(CT.CityName,'''') AS ''City'',\n" +
                        "    CONCAT(S.FirstName, '' '', S.LastName) AS ''Field Staff Name'',\n" +
                        "    ISNULL(SDE.ActualExpense, 0) AS ''Actual Expense'',\n" +
                        "    DS.Status AS ''DPR Status'',\n" +
                        "    ' + @ColumnList + ',\n" +
                        "    DP.TotalActualExpense AS ''Day Total''\n" +
                        "FROM Staff S\n" +
                        "JOIN StaffExpenseTransaction ST ON S.StaffId = ST.StaffId \n" +
                        "AND S.StaffId IN (\n" +
                        "    SELECT DISTINCT s.StaffId\n" +
                        "    FROM Staff s \n" +
                        "    LEFT JOIN Users u ON u.FirstName = s.FirstName AND u.LastName = s.LastName\n" +
                        "    WHERE s.ProjectCoordinator = @StaffId\n" +
                        "    AND s.StaffId IS NOT NULL \n" +
                        "    AND u.IsLoginActive <> 0 \n" +
                        "    AND s.FirstName NOT LIKE ''%Test%'' \n" +
                        "    AND s.FirstName NOT LIKE ''%Admin%''\n" +
                        "    AND s.Active <> 0  \n" +
                        ")\n" +
                        "JOIN DailyProgressReport DP ON DP.FilledBy = S.StaffId \n" +
                        "AND DP.Date BETWEEN @FromDate AND @ToDate\n" +
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
                        "WHERE (P.ProjectId IS NULL OR CT.CityId IS NULL OR CT.CityId IN (\n" +
                        "    SELECT DISTINCT c.CityId \n" +
                        "    FROM ZoneMaster z \n" +
                        "    LEFT JOIN City c ON c.ZoneId = z.ZoneId\n" +
                        "    WHERE z.ProjectCoordinator LIKE ''%'' + CAST(@StaffId AS VARCHAR) + ''%''\n" +
                        "))\n" +
                        "GROUP BY\n" +
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
                        "EXEC sp_executesql @Sql,\n" +
                        "     N'@FromDate DATETIME, @ToDate DATETIME, @StaffId INT',\n" +
                        "      @FromDate,\n" +
                        "      @ToDate,\n" +
                        "      @StaffId;";
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

    public List< Map< String, Object > > fetchExpenseSheet( Integer year, Integer month, Integer staffId ) {

        try {

            // Construct the dynamic SQL
            String sql = "DECLARE @Month INT = "+month+";\n" +
                    "\n" +
                    "DECLARE @Year INT = "+year+";\n" +
                    "\n" +
                    "DECLARE @ProjectCoordinatorId INT = "+staffId+";\n" +
                    "\n" +
                    "DECLARE @DateStaffRanges TABLE (FromDate DATE, ToDate DATE, StaffId INT, OpeningBalance FLOAT, ClosingBalance FLOAT, Tea FLOAT, -- Add Tea column\n" +
                    " Telephone FLOAT, -- Add Telephone column\n" +
                    " Petrol FLOAT -- Add Petrol column\n" +
                    ");\n" +
                    "\n" +
                    "-- Dynamically insert date ranges and staff IDs from MonthlyOpeningExpense\n" +
                    "\n" +
                    "INSERT INTO @DateStaffRanges (FromDate, ToDate, StaffId, OpeningBalance, ClosingBalance, Tea, Telephone, Petrol)\n" +
                    "SELECT MOE.OpeningDate AS FromDate,\n" +
                    "       MOE.ClosingDate AS ToDate,\n" +
                    "       MOE.StaffId,\n" +
                    "       MOE.OpeningBalance,\n" +
                    "       MOE.ClosingBalance,\n" +
                    "       ISNULL(ME.Tea, 0) AS Tea,\n" +
                    "       ISNULL(ME.Telephone, 0) AS Telephone,\n" +
                    "       ISNULL(ME.Petrol, 0) AS Petrol\n" +
                    "FROM MonthlyOpeningExpense MOE\n" +
                    "LEFT JOIN MonthlyExpense ME ON ME.Month = MOE.Month\n" +
                    "AND ME.Year = MOE.Year\n" +
                    "AND MOE.ProjectCoordinator = ME.ProjectCoordinator\n" +
                    "AND MOE.StaffId = ME.StaffId\n" +
                    "WHERE MOE.ProjectCoordinator = @ProjectCoordinatorId\n" +
                    "  AND MOE.Month = @Month\n" +
                    "  AND MOE.Year = @Year;\n" +
                    "\n" +
                    "-- Recursive CTE to expand the date ranges\n" +
                    ";\n" +
                    "\n" +
                    "WITH DateRange AS\n" +
                    "  (-- Anchor member\n" +
                    " SELECT DR.FromDate AS StartDate,\n" +
                    "        DR.ToDate AS EndDate,\n" +
                    "        DR.StaffId,\n" +
                    "        DR.OpeningBalance,\n" +
                    "        DR.ClosingBalance,\n" +
                    "        DR.Tea,\n" +
                    "        DR.Telephone,\n" +
                    "        DR.Petrol,\n" +
                    "        CAST(DR.FromDate AS DATE) AS [Date]\n" +
                    "   FROM @DateStaffRanges DR\n" +
                    "   UNION ALL -- Recursive member\n" +
                    " SELECT DR.StartDate,\n" +
                    "        DR.EndDate,\n" +
                    "        DR.StaffId,\n" +
                    "        DR.OpeningBalance,\n" +
                    "        DR.ClosingBalance,\n" +
                    "        DR.Tea,\n" +
                    "        DR.Telephone,\n" +
                    "        DR.Petrol,\n" +
                    "        DATEADD(DAY, 1, DR.[Date]) AS [Date]\n" +
                    "   FROM DateRange DR\n" +
                    "   WHERE DATEADD(DAY, 1, DR.[Date]) <= DR.EndDate) -- Final query to retrieve data\n" +
                    "\n" +
                    "SELECT s.StaffId AS staffId,\n" +
                    "       CONCAT(TRIM(s.FirstName), ' ', TRIM(s.LastName)) AS staffName,\n" +
                    "       DR.[Date] AS date,\n" +
                    "       ISNULL(SUM(DP.TotalActualExpense), 0) AS totalActualExpense,\n" +
                    "       ISNULL(SUM(TRY_CAST(DP.amountCash AS float)), 0) AS amountCash, -- CASE statement to handle ApprovedBy condition\n" +
                    " CASE\n" +
                    "     WHEN ARE.ApprovedBy IN (1,\n" +
                    "                             7) THEN 0\n" +
                    "     ELSE ISNULL(SUM(ARE.approvedAmount), 0)\n" +
                    " END AS approvedAmount,\n" +
                    " ISNULL(ARE.ApprovedBy, 0) AS approvedBy,\n" +
                    " DR.StartDate AS openingDate,\n" +
                    " DR.EndDate AS closingDate,\n" +
                    " DR.OpeningBalance AS openingBalance,\n" +
                    " DR.ClosingBalance AS closingBalance,\n" +
                    " DR.Tea AS tea,\n" +
                    " DR.Telephone AS telephone,\n" +
                    " DR.Petrol AS petrol\n" +
                    "FROM DateRange DR\n" +
                    "LEFT JOIN Staff s ON s.StaffId = DR.StaffId\n" +
                    "LEFT JOIN\n" +
                    "  (SELECT CreatedBy,\n" +
                    "          ApprovedOn,\n" +
                    "          ApprovedBy,\n" +
                    "          SUM(CAST(ApprovedAmount AS float)) AS approvedAmount\n" +
                    "   FROM AdvanceRequestExpense\n" +
                    "   GROUP BY CreatedBy,\n" +
                    "            ApprovedOn,\n" +
                    "            ApprovedBy) ARE ON ARE.CreatedBy = s.StaffId\n" +
                    "AND CAST(ARE.ApprovedOn AS DATE) = DR.[Date]\n" +
                    "LEFT JOIN\n" +
                    "  (SELECT FilledBy,\n" +
                    "          [Date],\n" +
                    "          SUM(CAST(TotalActualExpense AS float)) AS TotalActualExpense,\n" +
                    "          ISNULL(SUM(TRY_CAST(Request_Amount_Cash AS float)), 0) AS amountCash\n" +
                    "   FROM DailyProgressReport\n" +
                    "   GROUP BY FilledBy,\n" +
                    "            [Date]) DP ON DP.FilledBy = s.StaffId\n" +
                    "AND CAST(DP.Date AS DATE) = DR.[Date]\n" +
                    "GROUP BY s.StaffId,\n" +
                    "         s.FirstName,\n" +
                    "         s.LastName,\n" +
                    "         DR.[Date],\n" +
                    "         ARE.ApprovedBy,\n" +
                    "         DR.StartDate,\n" +
                    "         DR.EndDate,\n" +
                    "         DR.OpeningBalance,\n" +
                    "         DR.ClosingBalance,\n" +
                    "         DR.Tea,\n" +
                    "         DR.Telephone,\n" +
                    "         DR.Petrol\n" +
                    "ORDER BY s.StaffId,\n" +
                    "         DR.[Date] OPTION (MAXRECURSION 0); -- Allow unlimited recursion if needed\n";
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

    public List< Map< String, Object > > fetchAdvanceForProCo( LocalDate fromDate, LocalDate toDate, Integer staffId ) {
        try {

            String sql = "DECLARE @FromDate DATE = CAST('"+fromDate +"' AS DATE);\n" +
                    "\n" +
                    "DECLARE @ToDate DATE = CAST('"+toDate +"' AS DATE);\n" +
                    "\n" +
                    "DECLARE @ProjectCoordinatorId INT = "+staffId +";\n" +
                    "\n" +
                    "WITH DateRange AS\n" +
                    "  (SELECT CAST(@FromDate AS DATE) AS [Date]\n" +
                    "   UNION ALL SELECT DATEADD(DAY, 1, [Date])\n" +
                    "   FROM DateRange\n" +
                    "   WHERE DATEADD(DAY, 1, [Date]) <= @ToDate)\n" +
                    "SELECT DR.[Date] AS date,\n" +
                    "       ISNULL(SUM(ARE.ApprovedAmount), 0) AS advanceAmount \n" +
                    "FROM DateRange DR\n" +
                    "LEFT JOIN AdvanceRequestExpense ARE ON DR.[Date] = CAST(ARE.ApprovedOn AS DATE)\n" +
                    "AND ARE.CreatedBy = @ProjectCoordinatorId\n" +
                    "AND (ARE.ApprovedBy = 1\n" +
                    "     OR ARE.ApprovedBy = 7)\n" +
                    "GROUP BY DR.[Date]\n" +
                    "ORDER BY DR.[Date] OPTION (MAXRECURSION 0); -- Ensure unlimited recursion for CTE\n";

            return jdbcTemplate.queryForList( sql );
        } catch (Exception e){
            e.printStackTrace( );
            return Collections.emptyList( );
        }
    }

    public List < Map < String, Object > > filterExpenseReport ( String startDate , String endDate , Integer projectCoordinator , Integer staffId , Integer city , Integer projectId ) {
        try {

            String sql = "DECLARE @FromDate DATETIME = '" + startDate + "';\n" +
                    "\n" +
                    "DECLARE @ToDate DATETIME = '" + endDate + "';\n" +
                    "\n" +
                    "DECLARE @ProjectId INT = " + projectId + ";\n" +
                    "\n" +
                    "DECLARE @StaffId INT = " + staffId + ";\n" +
                    "\n" +
                    "DECLARE @ProjectCoordinator INT = " + projectCoordinator + ";\n" +
                    "\n" +
                    "DECLARE @CityId INT = " + city + ";\n" +
                    "\n" +
                    "DECLARE @ColumnList NVARCHAR(MAX);\n" +
                    "\n" +
                    "-- Generate dynamic column list for expense categories\n" +
                    "\n" +
                    "SELECT @ColumnList = String_agg('MAX(CASE WHEN EX.ExpenseName = ''' + Replace(expensename, '''', '''''') + ''' THEN SQ.ActualAmount ELSE 0 END) AS [' + LEFT(expensename, 28) + ']', ', ')\n" +
                    "FROM expensecategory;\n" +
                    "\n" +
                    "DECLARE @Sql NVARCHAR(MAX);\n" +
                    "\n" +
                    "\n" +
                    "SET @Sql = 'SELECT DISTINCT DP.DPRId AS ''DPR No.'',\n" +
                    "\t\t\tDP.Date AS ''Date'',\n" +
                    "\t\t\tISNULL(PF.PortionName, '''') AS ''Floor Level'',\n" +
                    "\t\t\tISNULL(P.ProjectNumber, '''') AS ''Project No.'',\n" +
                    "\t\t\tISNULL(P.ProjectName, '''') AS ''Project'',\n" +
                    "\t\t\tISNULL(CT.CityName, '''') AS ''City'',\n" +
                    "\t\t\tCONCAT(pc.FirstName, '' '', pc.LastName) AS ''Project Coordinator'',\n" +
                    "\t\t\tCONCAT(S.FirstName, '' '', S.LastName) AS ''Field Staff Name'',\n" +
                    "\t\t\tISNULL(SDE.ActualExpense, 0) AS ''Actual Expense'',\n" +
                    "\t\t\tDS.Status AS ''DPR Status'', ' + @ColumnList + ',\n" +
                    "\t\t\tDP.TotalActualExpense AS ''Day Total'' \n" +
                    "\t\t\tFROM Staff S \n" +
                    "\t\t\tLEFT JOIN Staff pc ON S.ProjectCoordinator = pc.StaffId \n" +
                    "\t\t\tJOIN StaffExpenseTransaction ST ON S.StaffId = ST.StaffId  \n" +
                    "\t\t\tJOIN DailyProgressReport DP ON DP.FilledBy = S.StaffId  AND DP.Date BETWEEN @FromDate AND @ToDate \n" +
                    "\t\t\tLEFT JOIN Project P ON P.ProjectId = DP.ProjectId \n" +
                    "\t\t\tLEFT JOIN City CT ON CT.CityId = P.CityId \n" +
                    "\t\t\tLEFT JOIN PortionFloor PF ON PF.PortionId = DP.PortionId \n" +
                    "\t\t\tLEFT JOIN ( SELECT DPRId,\n" +
                    "\t\t\t\t\t\tSUM(ActualAmount) AS ActualExpense \n" +
                    "\t\t\t\t\t\tFROM DPRExpense GROUP BY DPRId ) SDE ON SDE.DPRId = DP.DPRId \n" +
                    "\t\t\tLEFT JOIN ( SELECT DPRId,\n" +
                    "\t\t\t\t\t\tExpenseCategoryId, \n" +
                    "\t\t\t\t\t\tActualAmount \n" +
                    "\t\t\t\t\t\tFROM DPRExpense ) SQ ON SQ.DPRId = DP.DPRId \n" +
                    "\t\t\tLEFT JOIN ExpenseCategory EX ON SQ.ExpenseCategoryId = EX.ExpenseCategoryId \n" +
                    "\t\t\tJOIN DPRStatus DS ON DS.StatusId = DP.DPRStatus\t\t\n" +
                    "\t\t\t\n" +
                    "\t\t\t";

            StringBuilder result = new StringBuilder ();
            result.append ( "WHERE " );

            if ( projectCoordinator != null ) {
                result.append ( "S.StaffId IN(SELECT DISTINCT s.StaffId FROM Staff s\n" +
                        "                        LEFT JOIN Users u ON u.FirstName = s.FirstName AND u.LastName = s.LastName \n" +
                        "                        WHERE s.ProjectCoordinator = @ProjectCoordinator\n" +
                        "                        AND s.StaffId IS NOT NULL\n" +
                        "                        AND u.IsLoginActive <> 0\n" +
                        "                        AND s.FirstName NOT LIKE ''%Test%''\n" +
                        "                        AND s.FirstName NOT LIKE ''%Admin%''\n" +
                        "                        AND s.Active <> 0)\n" +
                        "                        AND (P.ProjectId IS NULL OR CT.CityId IS NULL OR CT.CityId IN ( SELECT DISTINCT c.CityId  \n" +
                        "                        FROM ZoneMaster z  LEFT JOIN City c ON c.ZoneId = z.ZoneId \n" +
                        "                        WHERE z.ProjectCoordinator LIKE ''%'' + CAST(@ProjectCoordinator AS VARCHAR) + ''%'' ))" ).append ( " AND " );
            }
            if ( staffId != null ) {
                result.append ( "S.StaffId = @StaffId" ).append ( " AND " );
            }
            if ( city != null ) {
                result.append ( "CT.CityId = @CityId" ).append ( " AND " );
            }
            if ( projectId != null ) {
                result.append ( "DP.ProjectId=@ProjectID" ).append ( " AND " );
            }


            if ( !Objects.equals ( result.toString () , "WHERE " ) ) {
                result.setLength ( result.length () - 4 );
                sql = sql + result.toString ();
            }


            sql = sql + ( "\n" +
                    "\t\t\tGROUP BY \n" +
                    "\t\t\t\tDP.DPRId,\n" +
                    "\t\t\t\tDP.Date,\n" +
                    "\t\t\t\tP.ProjectNumber,\n" +
                    "\t\t\t\tP.ProjectName,\n" +
                    "\t\t\t\tPF.PortionName,\n" +
                    "\t\t\t\tCT.CityName,\n" +
                    "\t\t\t\tCONCAT(pc.FirstName, '' '', pc.LastName),\n" +
                    "\t\t\t\tCONCAT(S.FirstName, '' '', S.LastName),\n" +
                    "\t\t\t\tDS.Status, \n" +
                    "\t\t\t\tSDE.ActualExpense,\n" +
                    "\t\t\t\tDP.TotalActualExpense\n" +
                    "\t\t\tORDER BY DP.Date ASC;';\n" +
                    "\n" +
                    "PRINT @Sql;\n" +
                    "\n" +
                    "-- Execute the dynamically generated SQL query\n" +
                    "EXEC Sp_executesql @Sql,\n" +
                    "     N'@FromDate DATETIME, @ToDate DATETIME,@ProjectCoordinator INT,@StaffId INT,@ProjectId INT,@CityId INT',\n" +
                    "      @FromDate,\n" +
                    "      @ToDate,\n" +
                    "      @ProjectCoordinator,\n" +
                    "      @StaffId,\n" +
                    "      @ProjectId,\n" +
                    "      @CityId;" );

            return jdbcTemplate.queryForList ( sql );
        } catch ( Exception e ) {
            e.printStackTrace ();
            return Collections.emptyList ();
        }
    }

}