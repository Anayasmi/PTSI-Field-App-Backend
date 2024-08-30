package com.ptsi.report.service;

import com.ptsi.report.entity.Users;
import com.ptsi.report.model.request.LoginRequest;
import com.ptsi.report.model.response.LoginResponse;

import java.util.List;

public interface UsersService {

    LoginResponse login( LoginRequest loginRequest );

    List< Users > fetchUsers();
}
