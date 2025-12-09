package com.iremayvaz.config;

import com.iremayvaz.model.jwt.AuthEntryPoint;
import com.iremayvaz.model.jwt.JwtAuthenticationFilter;
import com.iremayvaz.model.userDetails.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // spring Security aktif
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    // GÜVENLİK KURALLARI

    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String REFRESH_TOKEN = "/refresh-token";
    public static final String[] SWAGGER_PATHS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Gelen isteğin header'ını kontrol eder
    private final AppUserDetailsService appUserDetailsService; // AppUserDetailsService gelecek Spring Context'ten
    private final AuthEntryPoint authEntryPoint; // unauthenticated işlemler için 401 (UNAUTHORIZED) hataları üretir

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){ // Girdiğimiz şifreyi hash'ler
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Spring Security'nin ana konfigürasyonu
        http.csrf(csrf -> csrf.disable())// csrf devre dışı, çünkü ekstra CSRF token kontrolü gereksiz
                .authorizeHttpRequests(request -> request
                                // AUTH endpoint'leri
                                .requestMatchers(LOGIN, REGISTER, REFRESH_TOKEN).permitAll() // bu endpoint'lere herkes erişebilir
                                .requestMatchers(SWAGGER_PATHS).permitAll()
                                // Sadece ADMIN
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                // USER + ADMIN (sipariş, sepet vs.)
                                .requestMatchers("/order/**").hasAnyRole("USER", "ADMIN")
                                .anyRequest() // Eğer authenticated değilsen
                                .authenticated()) // Filter katmanına gireceksin! Authenticate olmalısın!
                .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Her istekte JWT bekler????????????????????????????????????????????????????
                .authenticationProvider(authenticationProvider()) // Kullanıcı doğrulama sağlayıcısını (DaoAuthenticationProvider) Security’ye tanıtıyorsun.
                .addFilterBefore(jwtAuthenticationFilter, // JWT kontrolü
                        UsernamePasswordAuthenticationFilter.class); // Gelen request’in header’ında JWT varsa doğrulanır.

        return http.build();
    }

    // Cross-Site Request Forgery : Siteler arası istek sahteciliği,
    // tarayıcıda banka oturumu açtın sonra yeni sekmede başka siteye girdin.
    // O site senmiş gibi bankaya istek atabilir.
}
