package com.ptsi.report.entity;

import com.ptsi.report.model.request.OpeningExpenseRequest;
import com.ptsi.report.model.response.OpeningExpenseResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table ( name = "MonthlyOpeningExpense")
public class MonthlyOpeningExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "monthly_opening_expense_seq")
    @SequenceGenerator(name = "monthly_opening_expense_seq", sequenceName = "MonthlyOpeningExpense_SEQ", allocationSize = 1)
    @Column ( name = "ExpenseId" )
    private Integer expenseId;

    @Column ( name = "Month" )
    private Integer month;

    @Column ( name = "Year" )
    private Integer year;

    @Column ( name = "StaffId" )
    private Float staffId;

    @Column ( name = "OpeningExpense" )
    private Float openingExpense;

    @Column ( name = "CreatedBy" )
    private Integer createdBy;

    @Column ( name = "CreatedDate" )
    private String createdDate;

    @Column ( name = "UpdatedBy" )
    private Integer updatedBy;

    @Column ( name = "UpdatedDate" )
    private String updatedDate;


    public MonthlyOpeningExpense ( OpeningExpenseRequest openingExpenseRequest , Integer createdBy , String createdDate ) {
        this.expenseId = openingExpenseRequest.getExpenseId ( );
        this.month = openingExpenseRequest.getMonth ( );
        this.year = openingExpenseRequest.getYear ( );
        this.staffId = openingExpenseRequest.getStaffId ( );
        this.openingExpense = openingExpenseRequest.getOpeningExpense ( );
        this.createdBy = ( openingExpenseRequest.getExpenseId ( ) == null ) ? openingExpenseRequest.getUpdatedBy ( ) : createdBy;
        this.createdDate = ( openingExpenseRequest.getExpenseId ( ) == null ) ? String.valueOf ( LocalDateTime.now ( ) ) : createdDate;
        this.updatedBy = openingExpenseRequest.getUpdatedBy ( );
        this.updatedDate = String.valueOf ( LocalDateTime.now ( ) );
    }

    public OpeningExpenseResponse toDto ( String staffName ) {
        return new OpeningExpenseResponse ( Float.valueOf (expenseId) , staffId , staffName , openingExpense );
    }
}
