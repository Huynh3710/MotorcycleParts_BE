package com.motorcycleparts.motorcycleparts_master.auth;

import com.motorcycleparts.motorcycleparts_master.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

//thông tin đăng ký từ người dùng
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
