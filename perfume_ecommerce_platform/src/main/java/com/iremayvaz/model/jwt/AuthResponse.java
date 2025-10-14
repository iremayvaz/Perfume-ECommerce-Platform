package com.iremayvaz.model.jwt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
}
