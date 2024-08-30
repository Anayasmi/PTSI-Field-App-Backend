package com.ptsi.report.entity;

import com.ptsi.report.model.response.ProjectResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table ( name = "Project" )
public class ProjectData {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    @Column ( name = "ProjectId" )
    private Long projectId;

    @Column ( name = "ProjectNumber" )
    private String projectNumber;

    @Column ( name = "ProjectName" )
    private String projectName;

    @Column ( name = "ClientId" )
    private Integer clientId;

    @Column ( name = "EORId" )
    private Integer eorId;

    @Column ( name = "ArchitectId" )
    private Integer architectId;

    @Column ( name = "Developer" )
    private String developer;

    @Column ( name = "CategoryId" )
    private Integer categoryId;

    @Column ( name = "SubCategoryId" )
    private Integer subCategoryId;

    @Column ( name = "Status" )
    private Integer status;

    @Column ( name = "Address" )
    private String address;

    @Column ( name = "CityId" )
    private Integer cityId;

    @Column ( name = "StateId" )
    private Integer stateId;

    @Column ( name = "CountryId" )
    private Integer countryId;

    @Column ( name = "Active" )
    private Integer active;

    @Column ( name = "CreatedBy" )
    private Integer createdBy;

    @Column ( name = "CreatedDate" )
    private String createdDate;

    @Column ( name = "UpdatedBy" )
    private Integer updatedBy;

    @Column ( name = "UpdatedDate" )
    private String updatedDate;

    @Column ( name = "ZoneId" )
    private Integer zoneId;

    @Column ( name = "MarketingPerson" )
    private Integer marketingPerson;

    @Column ( name = "ProjectNumberWithoutSp" )
    private String projectNumberWithoutSp;

    @Column ( name = "ProjectNameWithoutSp" )
    private String projectNameWithoutSp;

    @Column ( name = "ProjectCoordinator" )
    private Float projectCoordinator;

    public ProjectResponse toDTO ( String projectCoordinatorName ) {
        return new ProjectResponse ( projectId , projectNumber , projectName , projectCoordinator , projectName.trim ( ) + ( "(" ) + projectNumber + ")" , projectCoordinatorName );
    }
}
