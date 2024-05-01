package com.motorcycleparts.motorcycleparts_master.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AuthenticationResponse {
    //ánh xạ tên trường, chỉ định getter,setter cho biến
    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("refresh_token")
    private String refresh_token;
    private Long userId;
    private String role;
    private Long cartId;
//    private String userId;
    private String message;
    private Long customerId;

}
