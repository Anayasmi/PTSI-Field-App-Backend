package com.ptsi.report.entity;

import com.ptsi.report.model.response.StaffResponse;
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
@Table ( name = "Staff" )
public class Staff {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    @Column ( name = "StaffId" )
    private Float staffId;

    @Column ( name = "EmployeeId" )
    private String employeeId;

    @Column ( name = "FirstName" )
    private String firstName;

    @Column ( name = "LastName" )
    private String lastName;

    @Column ( name = "MobileNo" )
    private String mobileNo;

    @Column ( name = "Email" )
    private String email;

    @Column ( name = "Address" )
    private String address;

    @Column ( name = "CityId" )
    private Float cityId;

    @Column ( name = "StateId" )
    private Float stateId;

    @Column ( name = "CountryId" )
    private Float countryId;

    @Column ( name = "ESICNumber" )
    private String esicNumber;

    @Column ( name = "BloodGroup" )
    private String bloodGroup;

    @Column ( name = "BaseLocationId" )
    private Float baseLocationId;

    @Column ( name = "CategoryId" )
    private Float categoryId;

    @Column ( name = "Balance" )
    private Float balance;

    @Column ( name = "UserId" )
    private Float userId;

    @Column ( name = "Active" )
    private Float active;

    @Column ( name = "CreatedBy" )
    private Float createdBy;

    @Column ( name = "CreatedDate" )
    private LocalDateTime createdDate;

    @Column ( name = "UpdatedBy" )
    private Float updatedBy;

    @Column ( name = "UpdatedDate" )
    private LocalDateTime updatedDate;

    @Column ( name = "CTC" )
    private Float ctc;

    @Column ( name = "AccountBalance" )
    private Float accountBalance;

    @Column ( name = "ProjectCoordinator" )
    private Float projectCoordinator;

    @Column ( name = "Tea" )
    private Float tea;

    @Column ( name = "Telephone" )
    private Float telephone;

    @Column ( name = "Petrol" )
    private Float petrol;

    public StaffResponse toDto ( ) {
        return new StaffResponse ( staffId , employeeId , firstName.trim ( ).concat ( " " ).concat ( lastName ).trim ( ) , mobileNo , email , projectCoordinator , tea , telephone , petrol );
    }
}
