package com.ptsi.report.service.impl;

import com.ptsi.report.entity.Users;
import com.ptsi.report.model.request.LoginRequest;
import com.ptsi.report.model.response.LoginResponse;
import com.ptsi.report.repository.UsersRepository;
import com.ptsi.report.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    public LoginResponse login ( LoginRequest loginRequest ) {
        Users users= usersRepository.findByUserNameAndPassword ( loginRequest.getUserName (), loginRequest.getPassword ( ) ).orElseThrow ();
        return users.toDTO ();
    }

    @Override
    public List < Users > fetchUsers ( ) {
        return usersRepository.findAll ();
    }
}
