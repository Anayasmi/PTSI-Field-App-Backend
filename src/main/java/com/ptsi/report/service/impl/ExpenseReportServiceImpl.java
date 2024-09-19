package com.ptsi.report.service.impl;

import com.ptsi.report.constant.StaffType;
import com.ptsi.report.model.response.*;
import com.ptsi.report.repository.ExpenseReportRepository;
import com.ptsi.report.service.ExpenseReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ExpenseReportServiceImpl implements ExpenseReportService {

    private final ExpenseReportRepository expenseReportRepository;

    public List< Map< String, Object > > fetchExpenseReport( Integer year,Integer month, Float staffId, StaffType staffType ) {

        LocalDate localDate=LocalDate.of( year,month,1 );
        String firstDayOfMonth = localDate + " 00:00:00.000";
        String lastDayOfMonth = localDate.withDayOfMonth(localDate.lengthOfMonth()) + " 23:59:59.000";
        String staffName = String.valueOf( staffId );
        return expenseReportRepository.fetchExpenseReport( firstDayOfMonth, lastDayOfMonth, staffName, staffType );
    }

    public List< ExpenseSheetResponse > fetchExpenseSheet( Integer year, Integer month, Integer staffId,Double openingBalance ) {

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        List<Map<String,Object>> advanceProCo= expenseReportRepository.fetchAdvanceForProCo( firstDayOfMonth,lastDayOfMonth,staffId );

        List<AdvanceDto> advanceDtoList=advanceProCo.stream( ).map( e ->new AdvanceDto( (( Date ) e.get( "date" )).toLocalDate(),( ( Integer ) e.get( "advanceAmount" ) ))).toList( );

        List< Map< String, Object > > openingExpenseList = expenseReportRepository.fetchExpenseSheet( year, month, staffId );

        List< ExpenseSheetDto > expenseSheetDtos = convertToDto( openingExpenseList );


        List<LocalDate> localDates = Stream.iterate(firstDayOfMonth, date -> date.plusDays(1))
                .limit( ChronoUnit.DAYS.between(firstDayOfMonth, lastDayOfMonth) + 1)  // +1 to include the end date
                .toList( );
        return convert( expenseSheetDtos,advanceDtoList,localDates,openingBalance );
    }

    @Override
    public List< ExpenseSheetResponse > fetchByProjectCoordinator( Integer year,Integer month,Integer staffId ) {

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        List<Map<String,Object>> advanceProCo= expenseReportRepository.fetchAdvanceForProCo( firstDayOfMonth,lastDayOfMonth,staffId );

        List<AdvanceDto> advanceDtoList=advanceProCo.stream( ).map( e ->new AdvanceDto( (( Date ) e.get( "date" )).toLocalDate(),( ( Integer ) e.get( "advanceAmount" ) ))).toList( );

        List< Map< String, Object > > openingExpenseList = expenseReportRepository.fetchExpenseSheet( year, month, staffId );

        List< ExpenseSheetDto > expenseSheetDtos = convertToDto( openingExpenseList );

        return convert( expenseSheetDtos,advanceDtoList,new ArrayList<>(),0.0 );
    }

    List< ExpenseSheetResponse > convert( List< ExpenseSheetDto > expenseSheetDtos,List<AdvanceDto> advanceDtoList,List<LocalDate> localDates,Double openingBalance ) {

        List< ExpenseSheetResponse > expenseSheetResponses = new ArrayList<>( );
        List< ExpenseSheetDto > expenseSheetDtoStaff = setMissingDateData(expenseSheetDtos,localDates);
        for ( LocalDate localDate : localDates ) {
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
                        .opening( ( localDate.getDayOfMonth( ) == 1 ) ? expense.getOpeningBalance( ) : staffSheet.getClosing() )
                        .receipt( expense.getApprovedAmount( ) )
                        .expense( expense.getTotalActualExpense( ) + expense.getAmountCash() )
                        .closing( ((( localDate.getDayOfMonth( ) == 1 ) ? expense.getOpeningBalance( ) : staffSheet.getClosing() ) + expense.getApprovedAmount( ) ) - (expense.getTotalActualExpense( )+expense.getAmountCash()) )
                        .tea( expense.getTea() )
                        .petrol( expense.getPetrol() )
                        .telephone( expense.getTelephone() )
                        .build( ) );
            });
            AdvanceDto advanceDto = advanceDtoList.stream( ).filter( e -> e.getDate( ).equals( localDate ) ).toList( ).get( 0 );

            Double closingExpense = finalExpenseSheet.getClosingExpense();
            Double receiptExpense = staffSheetResponseList.stream().mapToDouble( StaffSheetResponse::getReceipt ).sum();
            Double openingExpense=( localDate.getDayOfMonth( ) == 1 ) ? openingBalance : closingExpense;
            expenseSheetResponses.add( ExpenseSheetResponse.builder( )
                    .day( localDate.getDayOfMonth( ) )
                    .date( localDate )
                    .openingExpense( openingExpense )
                    .advance( Double.valueOf( advanceDto.getAdvanceAmount( ) ) )
                    .closingExpense( (openingExpense+advanceDto.getAdvanceAmount())-receiptExpense )
                    .totalExpense( staffSheetResponseList.stream().mapToDouble( StaffSheetResponse::getExpense ).sum() )
                    .totalAdvance( staffSheetResponseList.stream().mapToDouble( StaffSheetResponse::getReceipt ).sum() )
//                    .tea( sheetDto.getTea() )
//                    .petrol( sheetDto.getPetrol() )
//                    .telephone( sheetDto.getTelephone() )
                    .tea(0.0)
                    .petrol( 0.0 )
                    .telephone( 0.0 )
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
                .approvedBy( ( Integer ) e.get( "approvedBy" ) )
                .amountCash( ( Double ) e.get( "amountCash" ) )
                .date( (( Date ) e.get( "date" )).toLocalDate() )
                .openingDate( (( Date ) e.get( "openingDate" )).toLocalDate() )
                .closingDate( (( Date ) e.get( "closingDate" )).toLocalDate() )
                .openingBalance( ( Double ) e.get( "openingBalance" ) )
                .closingBalance( ( Double ) e.get( "closingBalance" ) )
                .approvedAmount( ( Double ) e.get( "approvedAmount" ) )
                .totalActualExpense( ( Double ) e.get( "totalActualExpense" ) )
                .tea( ( Double ) e.get( "tea" ) )
                .telephone( ( Double ) e.get( "telephone" ) )
                .petrol( ( Double ) e.get( "petrol" ) )
                .build( ) ).collect( Collectors.toList( ) );
    }

    List< ExpenseSheetDto > setMissingDateData(List< ExpenseSheetDto > expenseSheetDtos,List<LocalDate> localDates){
        List< ExpenseSheetDto > sheetDto=new ArrayList <>(  );
        List<Double> staffIds = expenseSheetDtos.stream().map( ExpenseSheetDto::getStaffId ).distinct().toList();

        for ( Double staffId:staffIds ){
            List<ExpenseSheetDto> expenseSheetDtoList = expenseSheetDtos.stream( ).filter( e->e.getStaffId().equals( staffId ) ).toList();
            for ( LocalDate date:localDates ) {
                List<ExpenseSheetDto> expenseSheet = expenseSheetDtoList.stream( ).filter( e->e.getDate().equals( date ) ).toList();
                if(expenseSheet.size() == 0){
                    ExpenseSheetDto expenseSheetDto = ExpenseSheetDto.builder( )
                            .staffName( expenseSheetDtoList.get( 0 ).getStaffName() )
                            .staffId( staffId )
                            .approvedBy( 0 )
                            .amountCash( 0.0 )
                            .date( date )
                            .openingDate( expenseSheetDtoList.get( 0 ).getOpeningDate() )
                            .closingDate( expenseSheetDtoList.get( 0 ).getClosingDate() )
                            .openingBalance( expenseSheetDtoList.get( 0 ).getOpeningBalance() )
                            .closingBalance( expenseSheetDtoList.get( 0 ).getClosingBalance() )
                            .approvedAmount( 0.0 )
                            .totalActualExpense( 0.0 )
                            .tea( expenseSheetDtoList.get( 0 ).getTea() )
                            .telephone( expenseSheetDtoList.get( 0 ).getTelephone() )
                            .petrol( expenseSheetDtoList.get( 0 ).getPetrol() )
                            .build( );
                    sheetDto.add( expenseSheetDto );
                }else {
                    sheetDto.addAll( expenseSheet );
                }

            }
        }
        return sheetDto;

    }
}