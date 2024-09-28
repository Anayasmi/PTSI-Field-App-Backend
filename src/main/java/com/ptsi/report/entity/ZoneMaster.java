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
@Table( name = "ZoneMaster" )
public class ZoneMaster {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column ( name = "ZoneId" )
    private Float zoneId;

    @Column ( name = "Zone" )
    private String zoneName;

    @Column ( name = "projectCoordinator" )
    private String projectCoordinator;

    @Column ( name = "CreatedBy" )
    private Float createdBy;

    @Column ( name = "CreatedDate" )
    private LocalDateTime createdDate;

    @Column ( name = "UpdatedDate" )
    private LocalDateTime updatedDate;

    @Column ( name = "isDeleted" )
    private Float isDeleted;

}
