package com.ptsi.report.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String name;
    private String mobileNo;
    private String email;
    private Float role;
    private Float isLoginActive;
    private Integer staffId;
    private String userRole;
}
