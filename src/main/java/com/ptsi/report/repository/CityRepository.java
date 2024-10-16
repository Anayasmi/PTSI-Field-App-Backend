package com.ptsi.report.repository;

import com.ptsi.report.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CityRepository extends JpaRepository< City,Float > {


    @Query(value = "SELECT \n" +
            "  c.CityId AS cityId, \n" +
            "  c.CityName AS cityName \n" +
            "FROM \n" +
            "  City c WHERE c.active <> 0 \n" +
            " ORDER BY c.UpdatedDate DESC;",nativeQuery = true)
    List< Map<String,Object> > getCities();
}
