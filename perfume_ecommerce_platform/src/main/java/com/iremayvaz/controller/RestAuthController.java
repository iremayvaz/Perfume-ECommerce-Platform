package com.iremayvaz.controller;

import com.iremayvaz.model.dto.DtoUser;
import com.iremayvaz.model.dto.DtoUserInsert;
import com.iremayvaz.model.dto.MeResponse;
import com.iremayvaz.model.entity.User;
import com.iremayvaz.model.jwt.AuthRequest;
import com.iremayvaz.model.jwt.AuthResponse;
import com.iremayvaz.model.jwt.RefreshTokenRequest;
import com.iremayvaz.repository.UserRepository;
import com.iremayvaz.service.AuthService;
import com.iremayvaz.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@Tag(name = "Auth API", description = "Auth işlemleri")
@RestController
@RequiredArgsConstructor
public class RestAuthController{

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @Operation(description = "Kaydolma")
    @PostMapping("/register")
    public ResponseEntity<DtoUser> register(@RequestBody @Valid DtoUserInsert dtoUserInsert) {
        var newUser = authService.register(dtoUserInsert);
        return ResponseEntity.created(URI.create("/users/" + newUser.getEmail())).body(newUser);
    }

    @Operation(description = "Giriş")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest existingEmployee) {
        var newLogin = authService.login(existingEmployee);
        return ResponseEntity.accepted().body(newLogin);
    }

    @Operation(description = "RefreshToken oluşturma")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        var refresh = refreshTokenService.refreshToken(request);
        return ResponseEntity.accepted().body(refresh);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-only")
    public String adminEndpoint() {
        return "Bu sayfa sadece ADMIN!";
    }

    @Operation(description = "Oturum açmış kullanıcının bilgisi")
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        // 1) Auth var mı?
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        // 2) Email'i al (UserDetails.getUsername() ile aynı)
        String email = authentication.getName();

        // 3) DB'den kullanıcıyı bul
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        // 4) DTO dön (istersen field’ları artır)
        MeResponse dto = new MeResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );

        return ResponseEntity.ok(dto);
    }

}
