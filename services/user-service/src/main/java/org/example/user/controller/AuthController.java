package org.example.user.controller;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.example.user.dto.LoginRequest;
import org.example.user.dto.LoginResponse;
import org.example.user.dto.RegisterRequest;
import org.example.user.entity.User;
import org.example.user.security.JwtUtil;
import org.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService,
                          @Value("${jwt.secret:${JWT_SECRET:changeit-changeit-changeit-changeit}}") String secret,
                          @Value("${jwt.expiration:${JWT_EXPIRATION_MS:86400000}}") long expiration) {
        this.userService = userService;
        this.jwtUtil = new JwtUtil(secret, expiration);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        try {
            var u = userService.register(req.getUsername(), req.getPassword(), req.getNickname());
            String token = jwtUtil.generateToken(String.valueOf(u.getId()), u.getUsername(), Map.of());
            LoginResponse resp = toLoginResponse(u.getId(), u.getUsername(), u.getNickname(), u.getAvatarUrl(), token);
            return ResponseEntity.ok(resp);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "username_exists"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest req) {
        var opt = userService.validateCredentials(req.getUsername(), req.getPassword());
        if (opt.isPresent()) {
            var u = opt.get();
            String token = jwtUtil.generateToken(String.valueOf(u.getId()), u.getUsername(), Map.of());
            LoginResponse resp = toLoginResponse(u.getId(), u.getUsername(), u.getNickname(), u.getAvatarUrl(), token);
            return ResponseEntity.ok(resp);
        }
        return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = extractBearerToken(authHeader);
        if (token == null) return ResponseEntity.badRequest().body(Map.of("error", "missing_token"));
        userService.blacklistToken(token);
        return ResponseEntity.ok(Map.of("message", "logged_out"));
    }

    public static String extractBearerToken(String authHeader) {
        if (authHeader == null) return null;
        String prefix = "Bearer ";
        return authHeader.startsWith(prefix) ? authHeader.substring(prefix.length()) : null;
    }

    public LoginResponse toLoginResponse(Long id, String username, String nickname, String avatarUrl, String token) {
        LoginResponse resp = new LoginResponse();
        resp.setToken(token);
        resp.setUserId(String.valueOf(id));
        resp.setUsername(username);
        resp.setNickname(nickname);
        resp.setAvatarUrl(avatarUrl);
        return resp;
    }

    public Claims verify(String token) {
        return jwtUtil.parseToken(token);
    }
}