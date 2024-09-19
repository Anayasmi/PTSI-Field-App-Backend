package com.ptsi.report.repository;

import com.ptsi.report.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CityRepository extends JpaRepository< City,Float > {

    List< City > findByProjectCoordinator( Float projectCoordinator);

    @Query(value = "SELECT \n" +
            "  c.CityId AS cityId, \n" +
            "  c.CityName AS cityName, \n" +
            "  c.ProjectCoordinator AS projectCoordinator, \n" +
            "  CONCAT(\n" +
            "    TRIM(s.FirstName), \n" +
            "    ' ', \n" +
            "    TRIM(s.LastName)\n" +
            "  ) AS staffName \n" +
            "FROM \n" +
            "  City c \n" +
            "  LEFT JOIN Staff s ON s.StaffId = c.ProjectCoordinator ORDER BY c.UpdatedDate DESC;",nativeQuery = true)
    List< Map<String,Object> > getCities();
}
