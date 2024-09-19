package com.ptsi.report.util;

import com.ptsi.report.constant.ExpenseList;
import com.ptsi.report.constant.StaffExpense;
import com.ptsi.report.model.response.ExpenseResponse;
import com.ptsi.report.model.response.ExpenseSheetResponse;
import com.ptsi.report.model.response.StaffExpenseReport;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ExpenseSheetExcel {


    public static String SHEET_NAME = "project_sheet";

    public static ByteArrayInputStream dataToExcel( List< ExpenseSheetResponse > expenseResponses ) {
        try (Workbook workbook = new XSSFWorkbook( ); ByteArrayOutputStream out = new ByteArrayOutputStream( )) {

            Sheet sheet = workbook.createSheet( SHEET_NAME );

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontName( "Calibri(Body)" );
            headerFont.setFontHeight( ( short ) ( 11 * 20 ) );
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment( HorizontalAlignment.CENTER );

            CellStyle subHeaderStyle = workbook.createCellStyle();
            Font subHeaderFont = workbook.createFont();
            subHeaderFont.setBold(true);
            subHeaderFont.setFontName( "Calibri(Body)" );
            subHeaderFont.setFontHeight( ( short ) ( 10 * 20 ) );
            subHeaderStyle.setFont(subHeaderFont);
            subHeaderStyle.setAlignment( HorizontalAlignment.CENTER );

            createDynamicRow( sheet, headerStyle,subHeaderStyle, expenseResponses );
            workbook.write( out );

            return new ByteArrayInputStream( out.toByteArray( ) );

        } catch (IOException e){
            e.printStackTrace( );
            System.out.println( "Fail to import data excel" );
            return null;

        }
    }


    public static void createDynamicRow( Sheet sheet, CellStyle headerStyle,CellStyle subHeaderStyle, List< ExpenseSheetResponse > expenseResponses ) {


        AtomicReference< Integer > key = new AtomicReference<>( 0 );
        AtomicReference< Row > row = new AtomicReference<>( );

        expenseResponses.forEach( expense -> {
            if ( expense.getDay( ) == 1 ) {
                createFirstRow( sheet, headerStyle, expense );
                key.set( key.get( ) + 1 );

                createSecondRow( sheet, subHeaderStyle, expense );
                key.set( key.get( ) + 1 );
            }
            row.set( sheet.createRow( key.get( ) ) );

            AtomicReference< Cell > cell = new AtomicReference<>( );

            cell.set( row.get( ).createCell( 0 ) );
            cell.get( ).setCellValue( expense.getDay( ) );

            cell.set( row.get( ).createCell( 1 ) );
            cell.get( ).setCellValue( expense.getOpeningExpense( ) );

            cell.set( row.get( ).createCell( 2 ) );
            if ( expense.getAdvance() != 0.0 ) {
                cell.get( ).setCellValue( expense.getAdvance( ) );
            }


            AtomicReference< Integer > cellNumber = new AtomicReference<>( 2 );

            expense.getStaffSheetResponseList( ).forEach( staff -> {

                cellNumber.set( cellNumber.get( ) + 1 );

                cell.set( row.get( ).createCell( cellNumber.get( ) ) );
                if ( staff.getOpening() != null && staff.getOpening( ) != 0.0 ) {
                    cell.get( ).setCellValue( staff.getOpening( ) );

                }

                cellNumber.set( cellNumber.get( ) + 1 );
                cell.set( row.get( ).createCell( cellNumber.get( ) ) );
                if ( staff.getReceipt() != null && staff.getReceipt() != 0.0 ) {
                    cell.get( ).setCellValue( staff.getReceipt( ) );
                }

                cellNumber.set( cellNumber.get( ) + 1 );
                cell.set( row.get( ).createCell( cellNumber.get( ) ) );
                if ( staff.getExpense() != null && staff.getExpense() != 0.0 ) {
                    cell.get( ).setCellValue( staff.getExpense( ) );
                }

                cellNumber.set( cellNumber.get( ) + 1 );
                cell.set( row.get( ).createCell( cellNumber.get( ) ) );
                if ( staff.getClosing() != null && staff.getClosing() != 0.0 ) {
                    cell.get( ).setCellValue( staff.getClosing( ) );
                }

            } );

            cell.set( row.get( ).createCell( cellNumber.get( ) + 1 ) );
            if ( expense.getTotalExpense( ) != 0.0 ) {
                cell.get( ).setCellValue( expense.getTotalExpense( ) );
            }

            cell.set( row.get( ).createCell( cellNumber.get( ) + 2 ) );
            if ( expense.getClosingExpense( ) != 0.0 ) {
                cell.get( ).setCellValue( expense.getClosingExpense( ) );
            }

            cell.set( row.get( ).createCell( cellNumber.get( ) + 3 ) );
            if ( expense.getTotalAdvance() != 0.0 ) {
                cell.get( ).setCellValue( expense.getTotalAdvance( ) );
            }

            key.set( key.get( ) + 1 );

        } );
        createRowForTeaOrTelephoneOrPetrol(sheet,expenseResponses.get( 0 ),key.get()+1);
    }

    public static void createFirstRow( Sheet sheet, CellStyle cellStyle, ExpenseSheetResponse expenseResponse ) {

        Row row = sheet.createRow( 0 );

        AtomicReference< Cell > cell = new AtomicReference<>( );

        cell.set( row.createCell( 0 ) );
        cell.get( ).setCellValue( "Date" );
        cell.get( ).setCellStyle( cellStyle );
        sheet.setColumnWidth ( 0,12 * 256 );

        cell.set( row.createCell( 1 ) );
        cell.get( ).setCellValue( "Opening" );
        cell.get( ).setCellStyle( cellStyle );
        sheet.setColumnWidth ( 1,12 * 256 );

        cell.set( row.createCell( 2 ) );
        cell.get( ).setCellValue( "Advance" );
        cell.get( ).setCellStyle( cellStyle );
        sheet.setColumnWidth ( 2,12 * 256 );

        AtomicReference< Integer > number = new AtomicReference<>( 2 );
        expenseResponse.getStaffSheetResponseList( ).forEach( staff -> {

            number.set( number.get( ) + 1 );

            cell.set( row.createCell( number.get( ) ) );
            cell.get( ).setCellValue( staff.getStaffName( ) );
            cell.get( ).setCellStyle( cellStyle );
            sheet.addMergedRegion( new CellRangeAddress( 0, 0, number.get( ), number.get( ) + 3 ) );
            number.set( number.get( ) + 3 );
        } );

        cell.set( row.createCell( number.get( ) + 1 ) );
        cell.get( ).setCellValue( "Total Expense" );
        cell.get( ).setCellStyle( cellStyle );
        sheet.addMergedRegion( new CellRangeAddress( 0, 1, number.get( ) + 1, number.get( ) + 1 ) );
        sheet.setColumnWidth ( number.get()+1,14 * 256 );

        cell.set( row.createCell( number.get( ) + 2 ) );
        cell.get( ).setCellValue( "Closing" );
        cell.get( ).setCellStyle( cellStyle );
        sheet.addMergedRegion( new CellRangeAddress( 0, 1, number.get( ) + 2, number.get( ) + 2 ) );
        sheet.setColumnWidth ( number.get()+2,14 * 256 );

        cell.set( row.createCell( number.get( ) + 3 ) );
        cell.get( ).setCellValue( "Total Advance" );
        cell.get( ).setCellStyle( cellStyle );
        sheet.addMergedRegion( new CellRangeAddress( 0, 1, number.get( ) + 3, number.get( ) + 3 ) );
        sheet.setColumnWidth ( number.get()+3,14 * 256 );

    }

    public static void createSecondRow( Sheet sheet, CellStyle cellStyle, ExpenseSheetResponse expenseResponse ) {

        Row row = sheet.createRow( 1 );
        AtomicReference< Cell > cell = new AtomicReference<>( );

        LocalDate localDate = expenseResponse.getDate();

        cell.set( row.createCell( 1 ) );
        cell.get( ).setCellValue( localDate.getMonth()+"-"+localDate.getYear() );
        cell.get( ).setCellStyle( cellStyle );
        sheet.addMergedRegion( new CellRangeAddress( 1, 1, 1, 2 ) );


        AtomicReference< Integer > No = new AtomicReference<>( 2 );
        expenseResponse.getStaffSheetResponseList( ).forEach( staff -> {

            No.set( No.get( ) + 1 );
            cell.set( row.createCell( No.get( ) ) );
            cell.get( ).setCellValue( "Opening" );
            cell.get( ).setCellStyle( cellStyle );
            sheet.setColumnWidth ( No.get(),12 * 256 );

            No.set( No.get( ) + 1 );
            cell.set( row.createCell( No.get( ) ) );
            cell.get( ).setCellValue( "Receipt" );
            cell.get( ).setCellStyle( cellStyle );
            sheet.setColumnWidth ( No.get(),12 * 256 );

            No.set( No.get( ) + 1 );
            cell.set( row.createCell( No.get( ) ) );
            cell.get( ).setCellValue( "Expense" );
            cell.get( ).setCellStyle( cellStyle );
            sheet.setColumnWidth ( No.get(),12 * 256 );

            No.set( No.get( ) + 1 );
            cell.set( row.createCell( No.get( ) ) );
            cell.get( ).setCellValue( "Closing" );
            cell.get( ).setCellStyle( cellStyle );
            sheet.setColumnWidth ( No.get(),12 * 256 );
        } );

    }

    public static void createRowForTeaOrTelephoneOrPetrol( Sheet sheet, ExpenseSheetResponse expenseResponse, Integer key) {

        List<String> cellNames= Arrays.asList( "Tea","Telephone","Petrol" );

        for ( String cellName:cellNames ) {
            Row row = sheet.createRow( key );

            AtomicReference< Cell > cell = new AtomicReference<>( );

            cell.set( row.createCell( 0 ) );
            cell.get( ).setCellValue( cellName );

            cell.set( row.createCell( 1 ) );
            Double data= ( cellName.equalsIgnoreCase( "Tea" ) ) ? expenseResponse.getTea( ) : ( cellName.equalsIgnoreCase( "Telephone" ) ) ? expenseResponse.getTelephone( ) : expenseResponse.getPetrol( );
            if ( data != 0.0 ) {
                cell.get( ).setCellValue(data);
            }

            AtomicReference< Integer > cellNumber = new AtomicReference<>( 3 );

            expenseResponse.getStaffSheetResponseList( ).forEach( staff -> {

                cell.set( row.createCell( cellNumber.get( ) ) );
                Double value= ( cellName.equalsIgnoreCase( "Tea" ) ) ? staff.getTea( ) : ( cellName.equalsIgnoreCase( "Telephone" ) ) ? staff.getTelephone( ) : staff.getPetrol( );
                if ( value != 0.0 ) {
                    cell.get( ).setCellValue(value);
                }
                cellNumber.set( cellNumber.get( ) + 4 );

            } );
            key=key+1;
        }

    }
}