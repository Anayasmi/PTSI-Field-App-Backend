package com.ptsi.report.repository;

import com.ptsi.report.entity.ProjectData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDataRepository extends JpaRepository < ProjectData, Long > {

}