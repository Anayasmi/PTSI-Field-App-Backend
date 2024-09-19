package com.ptsi.report.entity;

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
@Table( name = "City" )
public class City {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column ( name = "CityId" )
    private Float cityId;

    @Column ( name = "CityName" )
    private String cityName;

    @Column ( name = "projectCoordinator" )
    private Float projectCoordinator;

    @Column ( name = "CreatedBy" )
    private Float createdBy;

    @Column ( name = "CreatedDate" )
    private LocalDateTime createdDate;

    @Column ( name = "UpdatedDate" )
    private LocalDateTime updatedDate;

    @Column ( name = "Active" )
    private Float active;

}
