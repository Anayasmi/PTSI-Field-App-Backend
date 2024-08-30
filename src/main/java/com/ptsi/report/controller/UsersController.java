package com.ptsi.report.controller;

import com.ptsi.report.entity.Users;
import com.ptsi.report.model.request.LoginRequest;
import com.ptsi.report.model.response.LoginResponse;
import com.ptsi.report.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin ( "*" )
@RequestMapping ( "/api/v1/users" )
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UsersService usersService;

    @PostMapping ( "/login" )
    public ResponseEntity < LoginResponse > login ( @RequestBody LoginRequest loginRequest ) {
        log.info( "login where user is {}",loginRequest.getUserName() );
        return new ResponseEntity <> ( usersService.login ( loginRequest ) , HttpStatus.ACCEPTED );
    }
}
