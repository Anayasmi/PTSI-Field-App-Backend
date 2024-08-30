package com.ptsi.report.repository;

import com.ptsi.report.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository< Users,Float > {

    Optional < Users > findByUserNameAndPassword ( String userName,String password );
}
