package com.tailtales.backend.jwt;

import com.tailtales.backend.config.EnvConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Log4j2
@Component
public class JwtUtil {

    @Autowired
    private EnvConfig envConfig;

    private final Key accessKey;
    private final long accessTokenValidityInMilliseconds;
    private final Key refreshKey;
    private final long refreshTokenValidityInMilliseconds;

    public JwtUtil(EnvConfig envConfig) {

        String secretKey = envConfig.getJwtSecretKey();
        String refreshSecretKey = envConfig.getJwtRefreshSecretKey();
        long accessTokenValiditySeconds = envConfig.getAccessTokenValiditySeconds();
        long refreshTokenValiditySeconds = envConfig.getRefreshTokenValiditySeconds();

        byte[] accessKeyBytes = Decoders.BASE64.decode(secretKey);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.accessTokenValidityInMilliseconds = accessTokenValiditySeconds * 1000;

        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecretKey);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
        this.refreshTokenValidityInMilliseconds = refreshTokenValiditySeconds * 1000;

    }

    // JWT 토큰 생성 메서드
    public String generateAccessToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(subject) // 토큰 제목 (일반적으로 사용자 ID)
                .setIssuedAt(now) // 토큰 발급 시간
                .setExpiration(expiryDate) // 토큰 만료 시간
                .signWith(accessKey, SignatureAlgorithm.HS256) // 서명 알고리즘과 Key
                .compact();
    }

    // Refresh JWT 토큰 생성 메서드
    public String generateRefreshToken() {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 토큰에서 Subject (일반적으로 사용자 ID) 추출 메서드
    public String getSubjectFromAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Refresh JWT 토큰에서 Subject 추출 (필요한 경우)
    public String getSubjectFromRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // JWT 토큰 유효성 검증 메서드
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 토큰이 유효하지 않은 경우 (만료, 변조 등)
            log.error("Invalid Access Token", e);
            return false;
        }
    }

    // Refresh JWT 토큰 유효성 검증 메서드
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(refreshKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid Refresh Token", e);
            return false;
        }
    }

    // Access JWT 토큰 만료 시간 추출 메서드
    public long getExpirationTimeFromAccessToken(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.getTime();
    }

    // Refresh JWT 토큰 만료 시간 추출 메서드
    public long getExpirationTimeFromRefreshToken(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.getTime();
    }

}
