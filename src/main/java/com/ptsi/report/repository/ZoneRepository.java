package com.ptsi.report.repository;

import com.ptsi.report.entity.ZoneMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZoneRepository extends JpaRepository< ZoneMaster,Float > {

    List<ZoneMaster> findByIsDeleted(Float isDeleted);

}
