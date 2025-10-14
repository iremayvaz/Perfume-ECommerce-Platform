package com.iremayvaz.model.jwt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class AuthRequest {
    private String email;
    private String password;
}
