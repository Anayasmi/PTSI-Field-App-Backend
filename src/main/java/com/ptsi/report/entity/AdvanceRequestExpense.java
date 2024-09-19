package com.ptsi.report.entity;

import com.ptsi.report.model.request.AdvanceExpenseRequest;
import com.ptsi.report.model.request.MonthlyExpenseRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "AdvanceRequestExpense" )
public class AdvanceRequestExpense {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column ( name = "AdvanceRequestId" )
    private Float advanceRequestId;

    @Column ( name = "AmountRequested" )
    private Float amountRequested;

    @Column ( name = "CreatedBy" )
    private Float createdBy;

    @Column ( name = "ApprovedAmount" )
    private Float approvedAmount;

    @Column ( name = "ApprovedBy" )
    private Float approvedBy;

    @Column ( name = "ApprovedOn" )
    private String approvedOn;

}
