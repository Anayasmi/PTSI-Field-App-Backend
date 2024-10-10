package com.ptsi.report.service;

import com.ptsi.report.model.request.LoginRequest;
import com.ptsi.report.model.request.UserRequest;
import com.ptsi.report.model.response.CreationResponse;
import com.ptsi.report.model.response.LoginResponse;
import com.ptsi.report.model.response.UserResponse;

import java.util.List;

public interface UsersService {

    LoginResponse login( LoginRequest loginRequest );

    CreationResponse  updateUserType ( UserRequest userRequest );

    List< UserResponse > fetchActiveUsers();
}
