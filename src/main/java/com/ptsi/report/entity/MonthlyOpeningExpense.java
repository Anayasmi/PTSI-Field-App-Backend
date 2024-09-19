package com.ptsi.report.entity;

import com.ptsi.report.model.request.OpeningExpenseRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    @Column ( name = "OpeningId" )
    private Integer openingId;

    @Column ( name = "ProjectCoordinator" )
    private Float projectCoordinator;

    @Column ( name = "StaffId" )
    private Float staffId;

    @Column ( name = "AdditionalStaff" )
    private Integer additionalStaff;

    @Column ( name = "Year" )
    private Integer year;

    @Column ( name = "Month" )
    private Integer month;

    @Column ( name = "OpeningDate" )
    private LocalDate openingDate;

    @Column ( name = "ClosingDate" )
    private LocalDate closingDate;

    @Column ( name = "OpeningBalance" )
    private Float openingBalance;

    @Column ( name = "ClosingBalance" )
    private Float closingBalance;

    @Column ( name = "CreatedBy" )
    private Integer createdBy;

    @Column ( name = "CreatedDate" )
    private String createdDate;

    @Column ( name = "UpdatedBy" )
    private Integer updatedBy;

    @Column ( name = "UpdatedDate" )
    private String updatedDate;

    @Column ( name = "AutoCreated" )
    private Integer autoCreated;




    public MonthlyOpeningExpense ( OpeningExpenseRequest openingExpenseRequest,Float closingBalance , Integer createdBy , String createdDate ) {

        this.autoCreated=openingExpenseRequest.getAutoCreated();
        this.openingBalance=openingExpenseRequest.getOpeningBalance();
        this.year=openingExpenseRequest.getYear();
        this.month=openingExpenseRequest.getMonth();
        this.openingId = openingExpenseRequest.getOpeningId ( );
        this.staffId = openingExpenseRequest.getStaffId ( );
        this.openingDate = openingExpenseRequest.getOpeningDate ( );
        this.closingDate=openingExpenseRequest.getClosingDate();
        this.projectCoordinator = openingExpenseRequest.getProjectCoordinator ( );
        this.createdBy = ( openingExpenseRequest.getOpeningId ( ) == null ) ? openingExpenseRequest.getUpdatedBy ( ) : createdBy;
        this.createdDate = ( openingExpenseRequest.getOpeningId ( ) == null ) ? String.valueOf ( LocalDateTime.now ( ) ) : createdDate;
        this.updatedBy = openingExpenseRequest.getUpdatedBy ( );
        this.updatedDate = String.valueOf ( LocalDateTime.now ( ) );
        this.closingBalance=closingBalance;
        this.additionalStaff=openingExpenseRequest.getAdditionalStaff();
    }
}
