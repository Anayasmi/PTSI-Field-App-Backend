package com.ptsi.report.service.impl;

import com.ptsi.report.constant.StaffCategory;
import com.ptsi.report.entity.MonthlyOpeningExpense;
import com.ptsi.report.model.request.OpeningExpenseRequest;
import com.ptsi.report.model.response.*;
import com.ptsi.report.repository.OpeningExpenseRepository;
import com.ptsi.report.service.OpeningExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OpeningExpenseServiceImpl implements OpeningExpenseService {

    private final OpeningExpenseRepository openingExpenseRepository;

    @Override
    public void saveOrUpdateOpeningExpense( OpeningExpenseRequest openingExpenseRequest ) {
        setExpense( openingExpenseRequest );
    }

    @Override
    public List < OpeningExpenseResponses > fetchByProjectCoordinator( Integer staffId , Integer month , Integer year , StaffCategory staffCategory ) {

        List < Map < String, Object > > openingExpenseList;

        LocalDate firstDayOfMonth = LocalDate.of( year , month , 1 );
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth( firstDayOfMonth.lengthOfMonth( ) );


        if ( staffCategory == StaffCategory.STAFF ) {
            openingExpenseList = openingExpenseRepository.findStaffBYProjectCoordinator( staffId , firstDayOfMonth , lastDayOfMonth );
        } else {
            openingExpenseList = openingExpenseRepository.findAdditionalStaffBYProjectCoordinator( staffId , firstDayOfMonth , lastDayOfMonth );
        }
        List < OpeningExpenseResponse > openingExpenseResponses = convertToDto( openingExpenseList );

        List < Double > staffIds = openingExpenseResponses.stream( ).map( OpeningExpenseResponse :: getStaffId ).distinct( ).toList( );

        List < OpeningExpenseResponses > responses = new ArrayList <>( );
        for ( Double id : staffIds ) {
            List < OpeningExpenseResponse > expenseResponses = openingExpenseResponses.stream( ).filter( e -> e.getStaffId( ).equals( id ) ).toList( );

            OpeningExpenseResponses expense = new OpeningExpenseResponses( );
            expense.setStaffId( id );
            expense.setCheck( expenseResponses.get( 0 ).getCheck( ) );
            expense.setStaffName( expenseResponses.get( 0 ).getStaffName( ) );
            expense.setOpeningBalance( expenseResponses.get( 0 ).getOpeningBalance( ) );
            expense.setClosingBalance( expenseResponses.get( expenseResponses.size( ) - 1 ).getClosingBalance( ) );
            expense.setOpeningExpensesList( expenseResponses.stream( )
                    .map( e -> new OpeningExpenses( e.getOpeningId( ) , e.getOpeningDate( ) , e.getOpeningBalance( ) , e.getClosingDate( ) , e.getClosingBalance( ) ) )
                    .collect( Collectors.toList( ) ) );
            responses.add( expense );
        }

        return responses;
    }

    @Override
    public void resetOpeningExpense( Integer projectCoordinator , Integer year , Integer month ) {
        LocalDate firstDayOfMonth = LocalDate.of( year , month , 1 );
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth( firstDayOfMonth.lengthOfMonth( ) );
        LocalDate previousMonth = LocalDate.of( year , month , 1 ).minusMonths( 1 );
        List < MonthlyOpeningExpense > previousMonthExpenses = openingExpenseRepository.findByProjectCoordinatorAndYearAndMonthAndAdditionalStaff( Float.valueOf( projectCoordinator ) , previousMonth.getYear( ) , previousMonth.getMonthValue( ),0 );
        List < MonthlyOpeningExpense > thisMonthExpenses = openingExpenseRepository.findByProjectCoordinatorAndYearAndMonthAndAdditionalStaff( Float.valueOf( projectCoordinator ) , year , month,0 );

        if ( previousMonthExpenses.size( ) == 0 && thisMonthExpenses.size( ) != 0 ) {
            List < Float > staffIds = thisMonthExpenses.stream( ).map( MonthlyOpeningExpense :: getStaffId ).distinct( ).toList( );
            for ( Float staffId : staffIds ) {
                List < MonthlyOpeningExpense > openingExpenseResponses = thisMonthExpenses.stream( ).filter( e -> e.getStaffId( ).equals( staffId ) ).toList( );

                openingExpenseResponses.forEach( monthlyOpeningExpense -> {
                    List < Map < String, Object > > expenses = openingExpenseRepository.findExpenseBYProjectCoordinatorAndStaff( Float.valueOf( projectCoordinator ) , staffId , monthlyOpeningExpense.getOpeningDate( ) , monthlyOpeningExpense.getClosingDate( ) );
                    List < AdvanceExpenseDto > advanceExpenses = convertToAdvanceDto( expenses );

                    AtomicReference < Float > openingBalance = new AtomicReference <>( monthlyOpeningExpense.getOpeningBalance( ) );
                    AtomicReference < Float > closingBalance = new AtomicReference <>( 0.0F );
                    advanceExpenses.forEach( advance -> {
                        closingBalance.set( ( float ) ( openingBalance.get( ) + advance.getApprovedAmount( ) - ( advance.getTotalActualExpense( ) + advance.getAmountCash( ) ) ) );
                        openingBalance.set( closingBalance.get( ) );
                    } );
                    monthlyOpeningExpense.setClosingBalance( closingBalance.get( ) );
                    monthlyOpeningExpense.setUpdatedDate( String.valueOf( LocalDate.now( ) ) );
                    monthlyOpeningExpense.setUpdatedBy( projectCoordinator );
                    openingExpenseRepository.save( monthlyOpeningExpense );
                } );
            }
        }

        if ( previousMonthExpenses.size( ) != 0 && thisMonthExpenses.size( ) == 0 ) {
            List < Float > staffIds = previousMonthExpenses.stream( ).map( MonthlyOpeningExpense :: getStaffId ).distinct( ).toList( );
            for ( Float staffId : staffIds ) {
                List < MonthlyOpeningExpense > openingExpenseResponses = previousMonthExpenses.stream( ).filter( e -> e.getStaffId( ).equals( staffId ) ).sorted( Comparator.comparing( MonthlyOpeningExpense :: getOpeningDate ).reversed( ) ).toList( );

                MonthlyOpeningExpense monthlyOpeningExpense = openingExpenseResponses.get( 0 );

                List < Map < String, Object > > expenses = openingExpenseRepository.findExpenseBYProjectCoordinatorAndStaff( Float.valueOf( projectCoordinator ) , staffId , firstDayOfMonth , lastDayOfMonth );
                List < AdvanceExpenseDto > advanceExpenses = convertToAdvanceDto( expenses );

                AtomicReference < Float > openingBalance = new AtomicReference <>( monthlyOpeningExpense.getOpeningBalance( ) );
                AtomicReference < Float > closingBalance = new AtomicReference <>( 0.0F );
                advanceExpenses.forEach( advance -> {
                    closingBalance.set( ( float ) ( openingBalance.get( ) + advance.getApprovedAmount( ) - ( advance.getTotalActualExpense( ) + advance.getAmountCash( ) ) ) );
                    openingBalance.set( closingBalance.get( ) );
                } );

                OpeningExpenseRequest openingExpenseRequest = OpeningExpenseRequest.builder( )
                        .year( year )
                        .month( month )
                        .openingId( null )
                        .openingDate( firstDayOfMonth )
                        .closingDate( lastDayOfMonth )
                        .openingBalance( monthlyOpeningExpense.getClosingBalance( ) )
                        .staffId( staffId )
                        .projectCoordinator( Float.valueOf( projectCoordinator ) )
                        .updatedBy( projectCoordinator )
                        .additionalStaff( 0 )
                        .build( );

                openingExpenseRepository.save( new MonthlyOpeningExpense( openingExpenseRequest , closingBalance.get( ) , projectCoordinator , String.valueOf( LocalDate.now( ) ) ) );

            }
        }

        if ( previousMonthExpenses.size( ) != 0 && thisMonthExpenses.size( ) != 0 ) {
            List < Float > staffs = thisMonthExpenses.stream( ).map( MonthlyOpeningExpense :: getStaffId ).distinct( ).toList( );
           List<Float> ids = previousMonthExpenses.stream( ).map( MonthlyOpeningExpense :: getStaffId ).distinct( ).toList( );
            List < Float > staffIds = new ArrayList<>();
            staffIds.addAll( staffs );
            staffIds.addAll( ids );
            staffIds=staffIds.stream().distinct().collect( Collectors.toList());

            for ( Float staffId : staffIds ) {
                List < MonthlyOpeningExpense > previousMonthOpeningExpenses = previousMonthExpenses.stream( ).filter( e -> e.getStaffId( ).equals( staffId ) ).sorted( Comparator.comparing( MonthlyOpeningExpense :: getOpeningDate ).reversed( ) ).toList( );

                MonthlyOpeningExpense previousMonthOpeningExpense = ( previousMonthOpeningExpenses.size( ) == 0 )? new MonthlyOpeningExpense( ) : previousMonthOpeningExpenses.get( 0 );

                List < MonthlyOpeningExpense > thisMonthOpeningExpenseResponses = thisMonthExpenses.stream( ).filter( e -> e.getStaffId( ).equals( staffId ) ).sorted( Comparator.comparing( MonthlyOpeningExpense :: getOpeningDate ) ).toList( );

                if(thisMonthOpeningExpenseResponses.size() == 0){
                    OpeningExpenseRequest openingExpenseRequest = OpeningExpenseRequest.builder( )
                            .year( year )
                            .month( month )
                            .openingId( null )
                            .openingDate( firstDayOfMonth )
                            .closingDate( lastDayOfMonth )
                            .openingBalance( previousMonthOpeningExpense.getClosingBalance() )
                            .staffId( staffId )
                            .projectCoordinator( Float.valueOf( projectCoordinator ) )
                            .updatedBy( projectCoordinator )
                            .additionalStaff( 0 )
                            .build( );
                    thisMonthOpeningExpenseResponses = new ArrayList<>();
                    thisMonthOpeningExpenseResponses.add( new MonthlyOpeningExpense( openingExpenseRequest , 0.0F , projectCoordinator , String.valueOf( LocalDate.now( ) ) ));
                }
                AtomicReference < Float > openingBalance = new AtomicReference <>( ( previousMonthOpeningExpense.getOpeningBalance( ) == null )? thisMonthOpeningExpenseResponses.get( 0 ).getOpeningBalance( ) : previousMonthOpeningExpense.getClosingBalance( ) );

                thisMonthOpeningExpenseResponses.forEach( monthlyOpeningExpense -> {

                    List < Map < String, Object > > expenses = openingExpenseRepository.findExpenseBYProjectCoordinatorAndStaff( Float.valueOf( projectCoordinator ) , staffId , monthlyOpeningExpense.getOpeningDate() , monthlyOpeningExpense.getClosingDate() );
                    List < AdvanceExpenseDto > advanceExpenses = convertToAdvanceDto( expenses );
                    AtomicReference < Float > openingExpense = new AtomicReference <>( openingBalance.get() );
                    AtomicReference < Float > closingBalance = new AtomicReference <>( 0.0F );
                    advanceExpenses.forEach( advance -> {
                        closingBalance.set( ( float ) ( openingExpense.get( ) + advance.getApprovedAmount( ) - ( advance.getTotalActualExpense( ) + advance.getAmountCash( ) ) ) );
                        openingExpense.set( closingBalance.get( ) );
                    } );
                    monthlyOpeningExpense.setOpeningBalance( openingBalance.get( ) );
                    monthlyOpeningExpense.setClosingBalance( closingBalance.get( ) );
                    monthlyOpeningExpense.setUpdatedDate( String.valueOf( LocalDate.now( ) ) );
                    monthlyOpeningExpense.setUpdatedBy( projectCoordinator );
                    openingExpenseRepository.save( monthlyOpeningExpense );
                    openingBalance.set( closingBalance.get( ) );
                } );
            }
        }

    }


    public List < OpeningExpenseResponse > convertToDto( List < Map < String, Object > > list ) {
        return list.stream( )
                .map( this :: mapToDto )
                .collect( Collectors.toList( ) );
    }

    public List < AdvanceExpenseDto > convertToAdvanceDto( List < Map < String, Object > > list ) {
        return list.stream( )
                .map( map -> AdvanceExpenseDto.builder( )
                        .staffName( getStringValue(  map.get( "staffName" ) ))
                        .staffId( getDoubleValue(  map.get( "staffId" ) ))
                        .date( getLocalDateValue(  map.get( "date" ) ))
                        .totalActualExpense( getDoubleValue(  map.get( "totalActualExpense" ) ))
                        .amountCash( getDoubleValue(  map.get( "amountCash" ) ))
                        .approvedAmount( getDoubleValue(  map.get( "approvedAmount" ) ))
                        .build( ) )
                .collect( Collectors.toList( ) );
    }

    private OpeningExpenseResponse mapToDto( Map < String, Object > map ) {
        return OpeningExpenseResponse.builder( )
                .staffName( getStringValue(  map.get( "staffName" ) ))
                .check( getIntegerValue(  map.get( "checkField" ) ))
                .staffId( getDoubleValue(  map.get( "staffId" ) ))
                .openingDate( ( ( map.get( "openingDate" ) ) == null )? null : getLocalDateValue(  map.get( "openingDate" ) ))
                .closingDate( ( ( map.get( "closingDate" ) ) == null )? null : getLocalDateValue( map.get( "closingDate" ) ))
                .openingBalance( getDoubleValue(  map.get( "openingBalance" ) ))
                .closingBalance( getDoubleValue(  map.get( "closingBalance" ) ))
                .openingId( getDoubleValue( map.get( "openingId" ) ))
                .build( );
    }


    void setExpense( OpeningExpenseRequest openingExpenseRequest ) {

        List < Map < String, Object > > expenses = openingExpenseRepository.findExpenseBYProjectCoordinatorAndStaff( openingExpenseRequest.getProjectCoordinator( ) , openingExpenseRequest.getStaffId( ) , openingExpenseRequest.getOpeningDate( ) , openingExpenseRequest.getClosingDate( ) );

        List<MonthlyOpeningExpense> monthlyOpeningExpenses = openingExpenseRepository.findByProjectCoordinatorAndStaffIdAndYearAndMonth( openingExpenseRequest.getProjectCoordinator( ) , openingExpenseRequest.getStaffId( ) , openingExpenseRequest.getYear( ) , openingExpenseRequest.getMonth( ) );

        List<LocalDate> localDates = new ArrayList<>();
        monthlyOpeningExpenses.forEach( monthlyOpeningExpense -> {
            if( !Objects.equals( openingExpenseRequest.getOpeningId( ) , monthlyOpeningExpense.getOpeningId( ) ) ) {
                localDates.addAll( Stream.iterate( monthlyOpeningExpense.getOpeningDate( ) , date -> date.plusDays( 1 ) )
                        .limit( ChronoUnit.DAYS.between( monthlyOpeningExpense.getOpeningDate( ) , monthlyOpeningExpense.getClosingDate( ) ) + 1 )  // +1 to include the end date
                        .toList( ) );
            }
        } );

        List<LocalDate> requestDates = Stream.iterate(openingExpenseRequest.getOpeningDate(), date -> date.plusDays(1))
                .limit( ChronoUnit.DAYS.between(openingExpenseRequest.getOpeningDate(), openingExpenseRequest.getClosingDate()) + 1)  // +1 to include the end date
                .toList( );
        requestDates.forEach( localDate -> {
            if(localDates.contains( localDate )){
                throw new RuntimeException();
            }
        } );


        List < AdvanceExpenseDto > advanceExpenseDto = convertToAdvanceDto( expenses );

        List < Double > staffIds = advanceExpenseDto.stream( ).map( AdvanceExpenseDto :: getStaffId ).distinct( ).toList( );

        for ( Double id : staffIds ) {

            if(openingExpenseRequest.getOpeningBalance() == null){
                monthlyOpeningExpenses = monthlyOpeningExpenses.stream().sorted(Comparator.comparing( MonthlyOpeningExpense::getOpeningDate ).reversed()).toList();
                openingExpenseRequest.setOpeningBalance(  monthlyOpeningExpenses.get( 0 ).getClosingBalance());
            }
            AtomicReference < Double > openingBalance=new AtomicReference <>( Double.valueOf( openingExpenseRequest.getOpeningBalance() ));


            List < AdvanceExpenseDto > advanceExpenses = advanceExpenseDto.stream( ).filter( e -> Objects.equals( e.getStaffId( ) , id ) ).toList( );

            AtomicReference < Double > closingBalance = new AtomicReference <>( 0.0 );
            advanceExpenses.forEach( advance -> {
                closingBalance.set(  openingBalance.get( ) + (advance.getApprovedAmount( )) - ( advance.getTotalActualExpense( ) + advance.getAmountCash( ) ) );
                openingBalance.set( closingBalance.get( ) );
            } );
            MonthlyOpeningExpense openingExpense = new MonthlyOpeningExpense( openingExpenseRequest , Float.valueOf( String.valueOf( closingBalance.get( ) ) ) , null , null );
            openingExpenseRepository.save( openingExpense );
        }
    }

    Double getDoubleValue(Object object){
        if(object instanceof Double) {
            return  ( Double ) object;
        }
        if ( object instanceof Float ){
            return Double.valueOf( ( Float ) object);
        }
        return 0.0;
    }

    Integer getIntegerValue(Object object){
        if(object instanceof Double) {
            return  Integer.valueOf( String.valueOf( object ) );
        }
        if ( object instanceof Float ){
            return Integer.valueOf( String.valueOf( object ) );
        }
        if ( object instanceof Integer ){
            return  ( Integer ) object;
        }
        return 0;
    }

    LocalDate getLocalDateValue(Object object){

        if ( object instanceof Timestamp ){
            Timestamp timestamp = (Timestamp ) object;
            return timestamp.toLocalDateTime( ).toLocalDate( );
        }
        if ( object instanceof Date ){
            Date date = (Date ) object;
            return date.toLocalDate( );
        }
        return LocalDate.now();
    }


    String getStringValue(Object object){
        if(object instanceof String){
            return  ( String ) object ;
        }
        return "";
    }

}
