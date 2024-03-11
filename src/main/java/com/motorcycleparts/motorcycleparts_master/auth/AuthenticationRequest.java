package com.motorcycleparts.motorcycleparts_master.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

//đăng nhập
public class AuthenticationRequest {
    private String email;
    private String password;
}
