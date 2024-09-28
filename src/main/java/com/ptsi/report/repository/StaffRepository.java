package com.ptsi.report.repository;

import com.ptsi.report.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface StaffRepository extends JpaRepository < Staff, Float > {


    @Query(value = "SELECT * FROM Staff s WHERE s.FirstName LIKE CONCAT('%', :firstName, '%') AND s.LastName LIKE CONCAT('%', :lastName, '%')", nativeQuery = true)
    List < Map < String, Object > > findUser( @Param("firstName") String firstName , @Param("lastName") String lastName );


    @Query(value = "SELECT s.StaffId AS staffId,\n" +
            "       CONCAT(TRIM(s.FirstName), ' ', TRIM(s.LastName)) AS staffName\n" +
            "FROM Staff s \n" +
            "LEFT JOIN Users u ON u.FirstName = s.FirstName\n" +
            "AND u.LastName = s.LastName\n" +
            "WHERE s.ProjectCoordinator != :staffId\n" +
            "  AND u.IsLoginActive <> 0\n" +
            "  AND s.FirstName Not Like '%Test%'\n" +
            "  AND s.FirstName Not Like '%Admin%'" +
            " AND s.Active <> 0 ;", nativeQuery = true)
    List < Map < String, Object > > findAllStaffNotInProjectCoordinator( Float staffId );

    List < Staff > findByStaffIdIn( List < Float > staffIds );


    @Query(value = "SELECT DISTINCT s.* \n" +
            "FROM Staff s \n" +
            "LEFT JOIN Users u ON u.FirstName = s.FirstName \n" +
            "AND u.LastName = s.LastName \n" +
            "WHERE u.IsLoginActive <> 0 \n" +
            "AND s.FirstName NOT LIKE '%Test%' \n" +
            "AND s.FirstName NOT LIKE '%Admin%'  \n" +
            "AND s.Active <> 0 \n" +
            "AND s.StaffId IS NOT NULL \n", nativeQuery = true)
    List < Staff > findActiveStaffByCoordinator( );
}
