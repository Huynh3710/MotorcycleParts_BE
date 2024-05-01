package com.motorcycleparts.motorcycleparts_master.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motorcycleparts.motorcycleparts_master.config.JwtService;
import com.motorcycleparts.motorcycleparts_master.model.Cart;
import com.motorcycleparts.motorcycleparts_master.model.Customer;
import com.motorcycleparts.motorcycleparts_master.model.user.Role;
import com.motorcycleparts.motorcycleparts_master.model.user.User;
import com.motorcycleparts.motorcycleparts_master.repository.CartRepository;
import com.motorcycleparts.motorcycleparts_master.repository.CustomerRepository;
import com.motorcycleparts.motorcycleparts_master.repository.TokenRepository;
import com.motorcycleparts.motorcycleparts_master.token.Token;
import com.motorcycleparts.motorcycleparts_master.token.TokenType;
import com.motorcycleparts.motorcycleparts_master.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        // Kiểm tra email đã tồn tại hay không
        Optional<User> existingUserByEmail = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUserByEmail.isPresent()) {
            return AuthenticationResponse.builder()
                    .message("Email đã được đăng ký")
                    .build();
        }
        // Kiểm tra số điện thoại nếu nó được cung cấp
        if(registerRequest.getPhoneNumber() != null){
            Optional<User> existingUserByPhoneNumber = userRepository.findByPhoneNumber(registerRequest.getPhoneNumber());
            if (existingUserByPhoneNumber.isPresent()) {
                return AuthenticationResponse.builder()
                        .message("Số điện thoại đã được đăng ký")
                        .build();
            }
        }

        //cart
        Cart cart = Cart.builder().orderDetails(new ArrayList<>()).build();
//        cartRepository.save(cart);
        //account
        var user = User.builder()
                .email(registerRequest.getEmail())
                .phoneNumber(registerRequest.getPhoneNumber())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole() == null ? Role.USER : registerRequest.getRole())
                .build();

        var customer = Customer.builder()
                .name(registerRequest.getFirstName() + " " + registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .avatar(registerRequest.getAvatar())
                .phoneNumber(registerRequest.getPhoneNumber())
                .sex(registerRequest.getSex())
                .user(user)
                .cart(cart)
                .build();


//        cart.setCustomer(customer);

        var saveCustomer = customerRepository.save(customer);
        var savedUser = saveCustomer.getUser();


        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        var token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
            tokenRepository.save(token);

        return AuthenticationResponse.builder()
                .access_token(jwtToken)
                .refresh_token(refreshToken)
                .userId(user.getId())
                .role(String.valueOf(user.getRole()))
                .cartId(cart.getId())
                .customerId(saveCustomer.getId())
                .build();
    }



    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();
        Customer customer = customerRepository.findByUser(user);
        Cart cart = customer.getCart();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        //all tokens must be revoked
        revokeAllUserTokens(user);

        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);

        return AuthenticationResponse.builder()
                .access_token(jwtToken)
                .refresh_token(refreshToken)
//                .id(user.getId())
                .role(String.valueOf(user.getRole()))
                .cartId(cart.getId())
                .userId(user.getId())
                .customerId(customer.getId())
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var userDetails = this.userRepository.findByEmail(userEmail).orElseThrow();

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                var accessToken = jwtService.generateToken(userDetails);

                revokeAllUserTokens(userDetails);

                var token = Token.builder()
                        .user(userDetails)
                        .token(accessToken)
                        .tokenType(TokenType.BEARER)
                        .expired(false)
                        .revoked(false)
                        .build();
                tokenRepository.save(token);

                var authResponse = AuthenticationResponse.builder()
                        .access_token(accessToken)
                        .refresh_token(refreshToken).build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
