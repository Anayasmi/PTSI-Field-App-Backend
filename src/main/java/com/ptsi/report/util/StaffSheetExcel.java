package com.ptsi.report.util;

import com.ptsi.report.constant.StaffExpense;
import com.ptsi.report.constant.StaffType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StaffSheetExcel {


    public static String SHEET_NAME = "project_sheet";

    public static ByteArrayInputStream dataToExcel ( List < Map<String,Object> > mapList, StaffType staffType ) {
        try ( Workbook workbook = new XSSFWorkbook ( ); ByteArrayOutputStream out = new ByteArrayOutputStream ( ) ) {
            Sheet sheet = workbook.createSheet ( SHEET_NAME );

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontName( "Calibri(Body)" );
            headerFont.setFontHeight( ( short ) ( 11 * 20 ) );
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment( HorizontalAlignment.CENTER );

            createFirstRow ( sheet , headerStyle,staffType );
            createStaffReportValueRow(sheet,mapList,workbook,staffType);
            workbook.write ( out );

            return new ByteArrayInputStream ( out.toByteArray ( ) );

        } catch ( IOException e ) {
            e.printStackTrace ( );
            System.out.println ( "Fail to import data excel" );
            return null;

        }
    }

    public static HashMap <Integer, String> headers ( StaffType staffType ) {
        HashMap <Integer, String> hashMap = new HashMap <> ( );
        int key = 0;
        StaffExpense[] expenses =StaffExpense.values ();
        List<StaffExpense> expenseList = new ArrayList <>(Arrays.asList(expenses));
        if(staffType == StaffType.PRO_CO){
            expenseList.remove( StaffExpense.PROJECT_COORDINATOR );
        }
        for (StaffExpense expense:expenseList){
            hashMap.put ( key , expense.getName () );
            key=key+1;
        }
        return hashMap;
    }

    public static void createFirstRow ( Sheet sheet , CellStyle cellStyle,StaffType staffType ) {

        Row row = sheet.createRow ( 0 );

        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setWrapText ( true );
        cellStyle.setAlignment ( HorizontalAlignment.LEFT );

        for ( Map.Entry <Integer, String> entry : headers (staffType).entrySet ( ) ) {
            Cell cell = row.createCell ( entry.getKey ( ) );
            cell.setCellValue ( entry.getValue ( ) );
            cell.setCellStyle ( cellStyle );
            sheet.setColumnWidth ( entry.getKey (),12 * 256 );
        }

    }

    public static void createStaffReportValueRow ( Sheet sheet,List<Map<String,Object>> mapList, Workbook workbook,StaffType staffType ) {

        CellStyle cellStyle = workbook.createCellStyle ( );

        AtomicInteger rowNo= new AtomicInteger ( 0 );
        for (Map<String,Object> staff : mapList) {
            rowNo.set ( rowNo.get ( ) + 1 );
            Row row = sheet.createRow ( rowNo.get ( ) );
            Cell cell;

            int key = 0;
            StaffExpense[] expenses = StaffExpense.values ( );

            List<StaffExpense> expenseList = new ArrayList <>(Arrays.asList(expenses));
            if(staffType == StaffType.PRO_CO){
                expenseList.remove( StaffExpense.PROJECT_COORDINATOR );
            }

            for (StaffExpense expense : expenseList) {
                cell = row.createCell ( key );
                cell.setCellStyle ( cellStyle );

                Object object=staff.get ( expense.getName () );

                if(object instanceof BigDecimal ){
                    Double value = ( ( BigDecimal ) object ).doubleValue ();
                    if(value != 0) {
                        cell.setCellValue( value );
                    }
                }

                if(object instanceof Integer){
                    Integer value = ( Integer ) object;
                    if(value != 0) {
                        cell.setCellValue( value );
                    }
                }
                if(object instanceof Double){
                    Double value = ( Double ) object;
                    if(value != 0) {
                        cell.setCellValue( value );
                    }
                }
                if(object instanceof String){
                    cell.setCellValue ( ( String ) object );
                }

                if ( object instanceof Timestamp ){
                    CellStyle style = workbook.createCellStyle ( );
                    CreationHelper creationHelper = workbook.getCreationHelper();
                    style.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));
                    cell.setCellStyle ( style );
                    cell.setCellValue ( ( Timestamp ) object );
                    sheet.setColumnWidth ( key,18 * 256 );
                }
                key = key + 1;
            }
        }
    }
}
