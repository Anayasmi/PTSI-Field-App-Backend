package com.ptsi.report.service.impl;

import com.ptsi.report.entity.Staff;
import com.ptsi.report.entity.Users;
import com.ptsi.report.model.request.LoginRequest;
import com.ptsi.report.model.response.LoginResponse;
import com.ptsi.report.repository.StaffRepository;
import com.ptsi.report.repository.UsersRepository;
import com.ptsi.report.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final StaffRepository staffRepository;

    @Override
    public LoginResponse login ( LoginRequest loginRequest ) {
        Users users= usersRepository.findByUserNameAndPassword ( loginRequest.getUserName (), loginRequest.getPassword ( ) ).orElseThrow ();
        List< Map <String,Object> > staffList = staffRepository.findUser(users.getFirstName().trim(),users.getLastName().trim() );
        Double staffId= (staffList.size() == 0) ? 0.0F : ( Double ) staffList.get( 0 ).get( "StaffId" );
        return users.toDTO (staffId);
    }

    @Override
    public List < Users > fetchUsers ( ) {
        return usersRepository.findAll ();
    }
}
