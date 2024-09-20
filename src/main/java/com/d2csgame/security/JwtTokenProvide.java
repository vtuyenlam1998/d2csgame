package com.d2csgame.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvide {

    // Key secret spring security
    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;

    // Thời gian hết hạn access token
    @Value("${spring.security.jwt.expiration}")
    private int jwtExpirationMs;

    // Thời gian hết hạn refresh token
    @Value(("${spring.security.jwt.refreshExpiration}"))
    private long jwtRefreshExpirationMs;

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    // Create Access Token from Authentication
    public String generateAccessToken(UserPrincipal userPrincipal) {

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Lấy tên quyền
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // chủ thẻ là username
                .claim("roles", userPrincipal.getAuthorities())
                .setIssuedAt(new Date()) // Thời điểm phát hành token
                .setExpiration(new Date((new Date().getTime() + jwtExpirationMs))) // Thời gian hết hạn
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // Ký tên với khóa bí mật
                .compact();
    }

    // Tạo Refresh Token
    public String generateRefreshToken(UserPrincipal userPrincipal) {
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())  // Chủ thể là username
                .setIssuedAt(new Date())  // Thời điểm phát hành token
                .setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs))  // Hết hạn sau 30 ngày
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)  // Ký token bằng khóa bí mật
                .compact();
    }

    public String getUserNameFromJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception exception) {

        }
        return false;
    }

}
