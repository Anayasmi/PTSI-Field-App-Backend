package com.ptsi.report.entity;

import com.ptsi.report.model.response.LoginResponse;
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
@Table ( name = "Users" )
public class Users {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    @Column ( name = "UserId" )
    private Float userId;

    @Column ( name = "FirstName" )
    private String firstName;

    @Column ( name = "LastName" )
    private String lastName;

    @Column ( name = "UserName" )
    private String userName;

    @Column ( name = "MobileNo" )
    private String mobileNo;

    @Column ( name = "Email" )
    private String email;

    @Column ( name = "Password" )
    private String password;

    @Column ( name = "Role" )
    private Float role;

    @Column ( name = "Active" )
    private Float active;

    @Column ( name = "IsLoginActive" )
    private Float isLoginActive;

    @Column ( name = "CreatedBy" )
    private Float createdBy;

    @Column ( name = "CreatedDate" )
    private LocalDateTime createdDate;

    @Column ( name = "UpdatedBy" )
    private Float updatedBy;

    @Column ( name = "UpdatedDate" )
    private LocalDateTime updatedDate;

    public LoginResponse toDTO (Double userId) {
        return new LoginResponse ( firstName.concat ( " " ).concat ( lastName ) , mobileNo , email , role , isLoginActive,userId );
    }
}
