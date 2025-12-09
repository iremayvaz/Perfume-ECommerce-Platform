package com.iremayvaz.model.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // JWT tabanlı kimlik doğrulama filtresi

    // OncePerRequestFilter, kullanıcıdan gelen isteğin Controller'a düşmeden kontrol edilmesini sağlar.
    // Her istek için bir kez çalışır

    private final JwtService jwtService; // Token çözmek için
    private final UserDetailsService userDetailsService; // User bilgilerini DB'den almak için

    @Override
    protected void doFilterInternal(HttpServletRequest request, // gelen istek
                                    HttpServletResponse response, // cevap
                                    FilterChain filterChain) // sıradaki servis
            throws ServletException, IOException {
        String header;
        String token;
        String username;

        // Authorization header'ı var mı kontrol eder.
        header = request.getHeader("Authorization"); // Ex. Bearer {token}

        if(header == null || !header.startsWith("Bearer ")){ // JWT'siz gelen istek
            filterChain.doFilter(request, response); // Filter katmanından geri döndün
            return;
        }

        // "Bearer " : 7 karakter
        token = header.substring(7); // 7. karakterden sonrası token

        try{
            username = jwtService.getUsernameByToken(token);
            if(username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null){ // Request daha önce doğrulanmamış

                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Kullanıcı DB'de var mı?

                if(userDetails != null && !jwtService.isTokenExpired(token)){
                    // Kişiyi SecurityContext'e koyabilirim.
                    // Bu da kullanıcıyı içeri alabilirim demek
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                                            null,
                                                                    userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication); // İstek authenticated
                    // En içe yazılmalı ki kullanıcılar rahat içeri giremesin
                    // YAZILMAZSA KATMAN ATLANAMAZ!
                }
            }
        } catch (ExpiredJwtException e){
            System.out.println("Token süresi dolmuştur : " + e.getMessage());
        } catch (Exception e){
            System.out.println("Genel bir hata oluştu : " + e.getMessage());
        }

        filterChain.doFilter(request, response); // Süreci devam ettir
    }
}
