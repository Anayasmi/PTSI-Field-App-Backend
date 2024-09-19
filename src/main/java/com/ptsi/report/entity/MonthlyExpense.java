package com.ptsi.report.entity;

import com.ptsi.report.model.request.MonthlyExpenseRequest;
import com.ptsi.report.model.request.OpeningExpenseRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table ( name = "MonthlyExpense")
public class MonthlyExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "monthly_expense_seq")
    @SequenceGenerator(name = "monthly_expense_seq", sequenceName = "MonthlyExpense_SEQ", allocationSize = 1)
    @Column ( name = "ExpenseId" )
    private Integer expenseId;

    @Column ( name = "ProjectCoordinator" )
    private Float projectCoordinator;

    @Column ( name = "Year" )
    private Integer year;

    @Column ( name = "Month" )
    private Integer month;

    @Column ( name = "AdditionalStaff" )
    private Integer additionalStaff;

    @Column ( name = "StaffId" )
    private Float staffId;

    @Column ( name = "Tea" )
    private Float tea;

    @Column ( name = "Telephone" )
    private Float telephone;

    @Column ( name = "Petrol" )
    private Float petrol;

    @Column ( name = "CreatedBy" )
    private Integer createdBy;

    @Column ( name = "CreatedDate" )
    private String createdDate;

    @Column ( name = "UpdatedBy" )
    private Integer updatedBy;

    @Column ( name = "UpdatedDate" )
    private String updatedDate;


    public MonthlyExpense( MonthlyExpenseRequest monthlyExpenseRequest , Integer createdBy , String createdDate ) {
        this.expenseId = monthlyExpenseRequest.getExpenseId ( );
        this.additionalStaff=monthlyExpenseRequest.getAdditionalStaff();
        this.projectCoordinator=monthlyExpenseRequest.getProjectCoordinator();
        this.month=monthlyExpenseRequest.getMonth();
        this.year=monthlyExpenseRequest.getYear();
        this.staffId = monthlyExpenseRequest.getStaffId ( );
        this.tea=monthlyExpenseRequest.getTea();
        this.telephone = monthlyExpenseRequest.getTelephone ( );
        this.petrol=monthlyExpenseRequest.getPetrol();
        this.createdBy = ( monthlyExpenseRequest.getExpenseId ( ) == null ) ? monthlyExpenseRequest.getUpdatedBy ( ) : createdBy;
        this.createdDate = ( monthlyExpenseRequest.getExpenseId ( ) == null ) ? String.valueOf ( LocalDateTime.now ( ) ) : createdDate;
        this.updatedBy = monthlyExpenseRequest.getUpdatedBy ( );
        this.updatedDate = String.valueOf ( LocalDateTime.now ( ) );
    }
}
