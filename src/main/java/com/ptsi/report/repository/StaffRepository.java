package com.ptsi.report.repository;

import com.ptsi.report.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface StaffRepository extends JpaRepository< Staff,Float > {

    @Query(value = "SELECT DISTINCT c.ProjectCoordinator As staffId,\n" +
            "                CONCAT(TRIM(s.FirstName), ' ', TRIM(s.LastName)) AS staffName\n" +
            "FROM City c\n" +
            "LEFT JOIN Staff s ON s.StaffId = c.ProjectCoordinator\n" +
            "WHERE c.ProjectCoordinator IS NOT NULL;",nativeQuery = true)
    List < Map<String,Object> > findByProCo();

    @Query(value = "SELECT s.StaffId AS staffId,\n" +
            "       CONCAT(TRIM(s.FirstName), ' ', TRIM(s.LastName)) AS staffName\n" +
            "FROM City C\n" +
            "LEFT JOIN [PTSI_APPLICATION].[dbo].Staff s ON s.CityId=C.CityId\n" +
            "LEFT JOIN Users u ON u.FirstName = s.FirstName\n" +
            "AND u.LastName = s.LastName\n" +
            "WHERE C.ProjectCoordinator != :staffId\n" +
            "  AND u.IsLoginActive <> 0\n" +
            "  AND s.FirstName Not Like '%Test%'\n" +
            "  AND s.FirstName Not Like '%Admin%'",nativeQuery = true)
    List < Map<String,Object> > findAllStaffNotInProjectCoordinator(Float staffId);
}
