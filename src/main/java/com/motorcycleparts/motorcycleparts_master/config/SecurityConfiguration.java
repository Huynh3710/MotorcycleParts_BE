package com.motorcycleparts.motorcycleparts_master.config;

import com.motorcycleparts.motorcycleparts_master.model.user.Permission;
import com.motorcycleparts.motorcycleparts_master.model.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry

                                //user kh có tk
                                .requestMatchers("/api/v1/authentication/**").permitAll()
                                .requestMatchers("/get-api/**").permitAll()
                                // 2 người admin và manager vòa được endpoint
                                .requestMatchers("/api/v1/demo-management/**").hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())

                                .requestMatchers(GET, "/api/v1/demo-management/**").hasAnyAuthority(Permission.ADMIN_READ.name(), Permission.MANAGER_READ.name())
                                .requestMatchers(POST, "/api/v1/demo-management/**").hasAnyAuthority(Permission.ADMIN_CREATE.name(), Permission.MANAGER_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/demo-management/**").hasAnyAuthority(Permission.ADMIN_UPDATE.name(), Permission.MANAGER_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/demo-management/**").hasAnyAuthority(Permission.ADMIN_DELETE.name(), Permission.MANAGER_DELETE.name())


                                //chỉ có admin mới truy cập được
                                .requestMatchers("/api/v1/demo-admin/**").hasRole(Role.ADMIN.name())

                                .requestMatchers(GET, "/api/v1/demo-admin/**").hasAuthority(Permission.ADMIN_READ.name())
                                .requestMatchers(POST, "/api/v1/demo-admin/**").hasAuthority(Permission.ADMIN_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/demo-admin/**").hasAuthority(Permission.ADMIN_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/demo-admin/**").hasAuthority(Permission.ADMIN_DELETE.name())

                                //user có tk (gọi khi đăng nhập (để bất cứ endpoint nào cũng được))
                                .anyRequest().authenticated()
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer
                                .logoutUrl("/api/v1/authentication/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) ->
                                        SecurityContextHolder.clearContext()));

        return http.build();
    }
}
