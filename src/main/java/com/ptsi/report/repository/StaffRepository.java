package com.ptsi.report.repository;

import com.ptsi.report.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository< Staff,Float > {
    List < Staff > findByProjectCoordinator( Float projectCoordinator);
}
