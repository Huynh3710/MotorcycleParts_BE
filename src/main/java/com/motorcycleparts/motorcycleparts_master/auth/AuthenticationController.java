package com.motorcycleparts.motorcycleparts_master.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController

//Chưa có tk gọi api/v1/authentication
@RequestMapping("api/v1/authentication")
@CrossOrigin ("http://localhost:3000")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest registerRequest
    ){
        return  ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    //login
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest
    ){
        return  ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));

    }

    @PostMapping("/refresh-token")
    public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

}
