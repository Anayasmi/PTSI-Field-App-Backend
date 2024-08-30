package com.ptsi.report.service.impl;

import com.ptsi.report.constant.StaffType;
import com.ptsi.report.entity.Staff;
import com.ptsi.report.model.response.*;
import com.ptsi.report.repository.ExpenseReportRepository;
import com.ptsi.report.repository.StaffRepository;
import com.ptsi.report.service.ExpenseReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseReportServiceImpl implements ExpenseReportService {

    private final ExpenseReportRepository expenseReportRepository;
    private final StaffRepository staffRepository;

    public List< Map< String, Object > > fetchExpenseReport( String fromDate, String toDate, Float staffId, StaffType staffType ) {

        fromDate = fromDate + " 00:00:00.000";
        toDate = toDate + " 23:59:59.000";
        String staffName = String.valueOf( staffId );
        if ( staffType == StaffType.PRO_CO ) {
            List< Float > ids = staffRepository.findByProjectCoordinator( Float.valueOf( staffId ) ).stream( ).map( Staff::getStaffId ).distinct( ).collect( Collectors.toList( ) );
            staffName = ( ids.size( ) == 0 ) ? null : ids.stream( ).map( String::valueOf ).collect( Collectors.joining( "," ) );
        }
        return expenseReportRepository.fetchExpenseReport( fromDate, toDate, staffName, staffType );
    }

    public List< ExpenseSheetResponse > fetchExpenseSheet( Integer year, Integer month, Integer staffId ) {

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        List< Map< String, Object > > openingExpenseList = expenseReportRepository.fetchExpenseSheet( firstDayOfMonth, lastDayOfMonth, staffId, year,month );

        List< ExpenseSheetDto > expenseSheetDtos = convertToDto( openingExpenseList );

        return convert( expenseSheetDtos, Double.valueOf( staffId ) );
    }

    @Override
    public List< ExpenseSheetResponse > fetchByProjectCoordinator( Integer year,Integer month,Integer staffId ) {

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        List< Map< String, Object > > openingExpenseList = expenseReportRepository.fetchExpenseSheet( firstDayOfMonth, lastDayOfMonth, staffId, year,month );

        List< ExpenseSheetDto > expenseSheetDtos = convertToDto( openingExpenseList );

        return convert( expenseSheetDtos, Double.valueOf( staffId ) );
    }

    List< ExpenseSheetResponse > convert( List< ExpenseSheetDto > expenseSheetDtos, Double staffId ) {

        List< ExpenseSheetResponse > expenseSheetResponses = new ArrayList<>( );
        List< LocalDate > dates = expenseSheetDtos.stream( ).map( ExpenseSheetDto::getDate ).distinct( ).sorted( ).collect( Collectors.toList( ) );
        List< ExpenseSheetDto > expenseSheetDtoProCo = expenseSheetDtos.stream( ).filter( e -> Objects.equals( e.getStaffId( ), staffId ) ).collect( Collectors.toList( ) );
        List< ExpenseSheetDto > expenseSheetDtoStaff = expenseSheetDtos;
        expenseSheetDtoStaff.removeAll( expenseSheetDtoProCo );
        for ( LocalDate localDate : dates ) {
            List< ExpenseSheetDto > expenseSheetDtoList = expenseSheetDtoStaff.stream( ).filter( e -> e.getDate( ).equals( localDate ) ).collect( Collectors.toList( ) );
            LocalDate previousDate=localDate.minusDays( 1 );
            ExpenseSheetResponse expenseSheet=new ExpenseSheetResponse(  );
            if(localDate.getDayOfMonth() != 1) {
                expenseSheet = expenseSheetResponses.stream( ).filter( e -> e.getDate( ).equals( previousDate ) ).collect( Collectors.toList( ) ).get( 0 );
            }
            List< StaffSheetResponse > staffSheetResponseList = new ArrayList<>( );

            ExpenseSheetResponse finalExpenseSheet = expenseSheet;
            expenseSheetDtoList.forEach( expense -> {
                StaffSheetResponse staffSheet=new StaffSheetResponse(  );
                if(localDate.getDayOfMonth() != 1) {
                    staffSheet = finalExpenseSheet.getStaffSheetResponseList( ).stream( ).filter( e -> e.getStaffId( ).equals( expense.getStaffId( ) ) ).collect( Collectors.toList( ) ).get( 0 );
                }
                staffSheetResponseList.add( StaffSheetResponse.builder( )
                        .staffName( expense.getStaffName( ) )
                        .staffId( expense.getStaffId() )
                        .opening( ( localDate.getDayOfMonth( ) == 1 ) ? expense.getOpeningExpense( ) : staffSheet.getClosing() )
                        .receipt( expense.getApprovedAmount( ) )
                        .expense( expense.getTotalActualExpense( ) )
                        .closing( ((( localDate.getDayOfMonth( ) == 1 ) ? expense.getOpeningExpense( ) : staffSheet.getClosing() ) + expense.getApprovedAmount( ) ) - expense.getTotalActualExpense( ) )
                        .tea( expense.getTea() )
                        .petrol( expense.getPetrol() )
                        .telephone( expense.getTelephone() )
                        .build( ) );
            });
            ExpenseSheetDto sheetDto = expenseSheetDtoProCo.stream( ).filter( e -> e.getDate( ).equals( localDate ) ).toList( ).get( 0 );

            Double closingExpense = finalExpenseSheet.getClosingExpense();
            Double receiptExpense = staffSheetResponseList.stream().mapToDouble( StaffSheetResponse::getReceipt ).sum();
            Double openingExpense=( localDate.getDayOfMonth( ) == 1 ) ? sheetDto.getOpeningExpense( ) : closingExpense;
            expenseSheetResponses.add( ExpenseSheetResponse.builder( )
                    .day( localDate.getDayOfMonth( ) )
                    .date( localDate )
                    .openingExpense( openingExpense )
                    .advance(  sheetDto.getApprovedAmount( ) )
                    .closingExpense( (openingExpense+sheetDto.getApprovedAmount())-receiptExpense )
                    .totalExpense( staffSheetResponseList.stream().mapToDouble( StaffSheetResponse::getExpense ).sum() )
                    .totalAdvance( staffSheetResponseList.stream().mapToDouble( StaffSheetResponse::getReceipt ).sum() )
                    .tea( sheetDto.getTea() )
                    .petrol( sheetDto.getPetrol() )
                    .telephone( sheetDto.getTelephone() )
                    .staffSheetResponseList( staffSheetResponseList.stream().sorted( Comparator.comparingDouble( StaffSheetResponse::getStaffId ) )
                            .collect(Collectors.toList()) )
                    .build( ) );
        }
        return expenseSheetResponses.stream( ).sorted(Comparator.comparingInt( ExpenseSheetResponse::getDay )).collect( Collectors.toList());
    }

    public List< ExpenseSheetDto > convertToDto( List< Map< String, Object > > list ) {
        return list.stream( ).map( e -> ExpenseSheetDto.builder( )
                .staffName( ( String ) e.get( "staffName" ) )
                .staffId( ( Double ) e.get( "staffId" ) )
                .date( (( Date ) e.get( "date" )).toLocalDate() )
                .approvedAmount( ( Integer ) e.get( "approvedAmount" ) )
                .totalActualExpense( ( Double ) e.get( "totalActualExpense" ) )
                .openingExpense( ( Double ) e.get( "openingExpense" ) )
                .tea( ( Double ) e.get( "tea" ) )
                .telephone( ( Double ) e.get( "telephone" ) )
                .petrol( ( Double ) e.get( "petrol" ) )
                .build( ) ).collect( Collectors.toList( ) );
    }
}