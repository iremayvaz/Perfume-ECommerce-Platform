package com.iremayvaz.config;

import com.iremayvaz.model.jwt.AuthEntryPoint;
import com.iremayvaz.model.jwt.JwtAuthenticationFilter;
import com.iremayvaz.model.userDetails.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity // spring Security aktif
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig { // GÜVENLİK KURALLARI
    public static final String ADMIN_LOGIN = "/admin/login";
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String REFRESH_TOKEN = "/refresh-token";
    public static final String ME = "/me";
    public static final String ADMIN_ONLY = "/admin-only";
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
    @Order(1)
    public SecurityFilterChain adminChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                ADMIN_LOGIN,
                                "/admin/logout",
                                "/css/**", "/js/**", "/images/**", "/webjars/**"
                        ).permitAll()
                        .anyRequest().hasRole("ADMIN")
                )
                // Admin tarafı session + form login
                .formLogin(form -> form
                        .loginPage(ADMIN_LOGIN)
                        .loginProcessingUrl(ADMIN_LOGIN)
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl(ADMIN_LOGIN + "?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl(ADMIN_LOGIN + "?logout")
                )
                .cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    .securityMatcher(
                        "/perfume/**",
                        "/cart/**",
                        "/order/**",
                        REGISTER, LOGIN, REFRESH_TOKEN, ME, ADMIN_ONLY)
                .csrf(csrf -> csrf.disable())// csrf devre dışı, çünkü ekstra CSRF token kontrolü gereksiz
                .authorizeHttpRequests(request -> request
                        .requestMatchers(LOGIN, REGISTER, REFRESH_TOKEN).permitAll() // bu endpoint'lere herkes erişebilir
                        .requestMatchers(SWAGGER_PATHS).permitAll()
                        .requestMatchers("/perfume/**").permitAll() // Ürün Listeleme, ürün detayı PUBLIC
                        .requestMatchers(ADMIN_ONLY).hasRole("ADMIN") // Sadece ADMIN
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
