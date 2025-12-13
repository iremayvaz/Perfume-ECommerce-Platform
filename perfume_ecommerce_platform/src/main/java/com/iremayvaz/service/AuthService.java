package com.iremayvaz.service;

import com.iremayvaz.model.dto.response.DtoUser;
import com.iremayvaz.model.dto.request.DtoUserInsert;
import com.iremayvaz.model.entity.RefreshToken;
import com.iremayvaz.model.entity.User;
import com.iremayvaz.model.enums.RoleName;
import com.iremayvaz.model.jwt.AuthRequest;
import com.iremayvaz.model.jwt.AuthResponse;
import com.iremayvaz.model.jwt.JwtService;
import com.iremayvaz.repository.RefreshTokenRepository;
import com.iremayvaz.repository.RoleRepository;
import com.iremayvaz.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    @Transactional // Bir metot veya sınıfın tamamını bir işlem sayar. Hata durumunda rollback yapar. (son committen sonraki tüm değişiklikler)
    public DtoUser register(DtoUserInsert dtoUserInsert){
        if (userRepository.findByEmail(dtoUserInsert.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email zaten kayıtlı");
        }

        User user = new User();
        user.setEmail(dtoUserInsert.getEmail());
        user.setPassword(passwordEncoder.encode(dtoUserInsert.getPassword()));

        // Eğer DtoUserInsert içinde varsa:
        user.setFirstName(dtoUserInsert.getFirstName());
        user.setLastName(dtoUserInsert.getLastName());
        user.setPhoneNumber(dtoUserInsert.getPhoneNum());

        // Default ROLE_USER ata
        var userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER bulunamadı, önce roles tablosuna ekle!"));
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);

        DtoUser dto = new DtoUser();
        // Şifreyi DTO’ya KOPYALAMA
        BeanUtils.copyProperties(savedUser, dto, "password");
        return dto;
    }


    private RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpireDate(new Date(System.currentTimeMillis() + 1000*60*60)); // 1 saat
        refreshToken.setUser(user);

        return refreshToken;
    }

    @Transactional
    public AuthResponse login(AuthRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()); // Email, şifre

        Authentication authentication = authenticationProvider.authenticate(authenticationToken); // AppUserDetailsService'ten DB'ye yükler ve şifreyi doğrular.

        var principal = (UserDetails) authentication.getPrincipal(); // doğrulanmış kullanıcı
        String accessToken = jwtService.generateToken(principal);

        var user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı!"));

        RefreshToken refreshToken = createRefreshToken(user);
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken, refreshToken.getRefreshToken());
    }
}
