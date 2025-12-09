package com.iremayvaz.model.jwt;

import com.iremayvaz.model.userDetails.AppUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService {

    public static final String SECRET_KEY = "R8HFcGTlOF8shhqFqp+o8FADLCohD6C5v2bHbfbQhnQ=";
    private static final Integer ACCESS_TTL = 1000*60*30; // 30 dakika

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claimsMap = buildClaims(userDetails);
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + ACCESS_TTL);

        return Jwts.builder() // Token oluşturucu başlat
                .setSubject(userDetails.getUsername()) // Email, token payload'una koyuluyor
                .addClaims(claimsMap)
                .setIssuedAt(now) // Token ne zaman oluşturuldu?
                .setExpiration(expiredDate) // Token ne kadar geçerli?
                .signWith(getKey(), // Token'ı oluştururken ve çözerken kullanılacak key
                        SignatureAlgorithm.HS256) // HMAC-SHA256 algosu ile imzala
                .compact(); // Token'ı string olarak dön
    }

    public Map<String, Object> buildClaims(UserDetails userDetails){ // Doğrulanmış kullanıcının token'ı içine koyulacak claim'leri hazırlar.
            Map<String, Object> claims = new HashMap<>();

            if (userDetails instanceof AppUserDetails appUserDetails) {
                claims.put("user_id", appUserDetails.getId());
                claims.put("roles", appUserDetails.getRoles().stream()
                        .map(Enum::name)        // ADMIN, USER
                        .toList()
                );
            } else {
                var all = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

                claims.put("roles", all);
            }

            return claims;
    }

    public Object getClaimsByKey(String token, String key){
        return getClaims(token).get(key);
    }

    public Claims getClaims(String token){
        // Claims, token içindeki payload{subject, expiration, custom alanlar}.
        // claimsFunction, hangi bilgiyi istiyorsan onu çıkarır.
        return Jwts.parserBuilder()
                .setSigningKey(getKey()) // Doğrulama için secret key
                .build()
                .parseClaimsJws(token) // Token'ı çöz
                .getBody(); // Payload kısmını al
    }

    public <T> T exportToken(String token, Function<Claims, T> claimsFunction){ // Token'ı çözmek için
        Claims claims = getClaims(token);
        return claimsFunction.apply(claims); // İstenen bilgiyi döndür
    }

    public String getUsernameByToken(String token){
        return exportToken(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token){
        Date expiredDate = exportToken(token, Claims::getExpiration);
        // Now: 15.40
        // expiredDate : 15.45
        return new Date().after(expiredDate); // şu anki zaman expiredDate'i geçtiyse TRUE yani token süresi dolmuş!
    }

    public Key getKey(){ // Token'ı oluşturacak ve çözecek "key"
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
