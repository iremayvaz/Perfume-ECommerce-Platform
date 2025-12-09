package com.iremayvaz.service;

import com.iremayvaz.model.entity.RefreshToken;
import com.iremayvaz.model.entity.User;
import com.iremayvaz.model.jwt.AuthResponse;
import com.iremayvaz.model.jwt.JwtService;
import com.iremayvaz.model.jwt.RefreshTokenRequest;
import com.iremayvaz.model.userDetails.AppUserDetails;
import com.iremayvaz.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public boolean isRefreshTokenExpired(Date expiredDate){
        return new Date().after(expiredDate); // Şu an, expireDate'ten sonra olmalı!
    }

    private RefreshToken createRefreshToken(User user){ // tek noktadan okunmalı (AuthService'ten CMD + V)
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpireDate(new Date(System.currentTimeMillis() + 1000*60*60*4)); // 4 saat
        refreshToken.setUser(user);

        return refreshToken;
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        var refreshToken = refreshTokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("REFRESH TOKEN GEÇERSİZ! " + request.getRefreshToken()));

        if(isRefreshTokenExpired(refreshToken.getExpireDate())){ // token expire olduysa TEMİZLENMELİ Mİ?
            throw new IllegalArgumentException("REFRESH TOKEN EXPIRE OLMUŞ : " + request.getRefreshToken());
        }

        var user = refreshToken.getUser();
        var principal = AppUserDetails.from(user);
        String accessToken = jwtService.generateToken(principal); // refreshToken ile yeni accessToken oluştu

        // Her yeni accessToken oluşturulduğunda refreshToken da yenilenmeli!
        RefreshToken newRefreshToken = refreshTokenRepository.save(createRefreshToken(refreshToken.getUser()));

        return new AuthResponse(accessToken, newRefreshToken.getRefreshToken());
    }
}
