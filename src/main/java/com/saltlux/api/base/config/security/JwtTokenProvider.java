package com.saltlux.api.base.config.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.saltlux.api.base.user.UserLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	// 설정 파일의 비밀키
	@Value("spring.jwt.secret")
	private String secretKey;

	private long tokenValidMilisecond = 1000L * 60 * 60; // 1시간만 토큰 유효

  @Autowired
	private UserLoginService userDetailsService;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	// jwt 토큰 생성
	public String createToken(String userPk, List<String> roles) {
		Claims claims = Jwts.claims().setSubject(userPk);
		claims.put("roles", roles);
		Date now = new Date();

		return Jwts.builder().setClaims(claims) // 데이터
				.setIssuedAt(now) // 토큰 발행 일자
				.setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // 토큰 만료 시간
				.signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘 및 비밀키 설정
				.compact();
	}

	// Jwt 토큰으로 인증 정보 조회
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());
	}

	// jwt 토큰에서 회원 구별 정보 추출
	public String getUserPk(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	// Request의 Header에서 Token 파싱 : "X-AUTH-TOKEN : jwt 토큰"
	public String resolveToken(HttpServletRequest req) {
		return req.getHeader("X-AUTH-TOKEN");
	}

	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
}
