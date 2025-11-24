package org.example.user.controller;

import io.jsonwebtoken.Claims;
import org.example.user.dto.PersonaUpdateRequest;
import org.example.user.dto.UpdateAuthorizationsRequest;
import org.example.user.dto.UpdateDevicesRequest;
import org.example.user.dto.UpdateProfileRequest;
import org.example.user.view.UserView;
import org.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.NoSuchElementException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthController authController;
    @Value("${user.avatar.dir:uploads/avatars}")
    private String avatarDir;

    public UserController(UserService userService, AuthController authController) {
        this.userService = userService;
        this.authController = authController;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        var token = AuthController.extractBearerToken(authHeader);
        if (token == null) return ResponseEntity.status(401).body(Map.of("error", "missing_token"));
        try {
            if (userService.isTokenBlacklisted(token)) return ResponseEntity.status(401).body(Map.of("error", "token_revoked"));
            Claims claims = authController.verify(token);
            Long userId = userService.resolveUserIdFromClaims(claims).orElseThrow();
            return userService.findById(userId)
                    .<ResponseEntity<?>>map(u -> ResponseEntity.ok(sanitize(u)))
                    .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "user_not_found")));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_token"));
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                           @RequestBody UpdateProfileRequest req) {
        var token = AuthController.extractBearerToken(authHeader);
        if (token == null) return ResponseEntity.status(401).body(Map.of("error", "missing_token"));
        try {
            if (userService.isTokenBlacklisted(token)) return ResponseEntity.status(401).body(Map.of("error", "token_revoked"));
            Claims claims = authController.verify(token);
            Long userId = userService.resolveUserIdFromClaims(claims).orElseThrow();
            UserView u = userService.updateProfile(userId, req.getNickname(), req.getAvatarUrl(), req.getEmail());
            return ResponseEntity.ok(sanitize(u));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", "user_not_found"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "server_error"));
        }
    }

    @PostMapping("/me/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                          @RequestParam("file") MultipartFile file) {
        var token = AuthController.extractBearerToken(authHeader);
        if (token == null) return ResponseEntity.status(401).body(Map.of("error", "missing_token"));
        try {
            if (userService.isTokenBlacklisted(token)) return ResponseEntity.status(401).body(Map.of("error", "token_revoked"));
            Claims claims = authController.verify(token);
            Long userId = userService.resolveUserIdFromClaims(claims).orElseThrow();
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "empty_file"));
            }

            String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
            String ext = ".png";
            String ct = file.getContentType();
            if (ct != null) {
                if (ct.contains("jpeg")) ext = ".jpg";
                else if (ct.contains("png")) ext = ".png";
                else if (ct.contains("webp")) ext = ".webp";
                else if (ct.contains("gif")) ext = ".gif";
            } else if (originalName.contains(".")) {
                ext = "." + originalName.substring(originalName.lastIndexOf('.') + 1);
            }

            Path base = Paths.get(avatarDir, String.valueOf(userId));
            Files.createDirectories(base);
            String filename = System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8) + ext;
            Path target = base.resolve(filename);
            Files.copy(file.getInputStream(), target);

            String url = "/user/users/avatars/" + userId + "/" + filename;
            UserView u = userService.updateProfile(userId, null, url, null);
            return ResponseEntity.ok(sanitize(u));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", "user_not_found"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "server_error"));
        }
    }

    @GetMapping("/avatars/{userId}/{filename}")
    public ResponseEntity<?> getAvatar(@PathVariable("userId") Long userId,
                                       @PathVariable("filename") String filename) {
        try {
            Path path = Paths.get(avatarDir, String.valueOf(userId), StringUtils.cleanPath(filename));
            if (!Files.exists(path)) return ResponseEntity.status(404).body(Map.of("error", "not_found"));
            Resource resource = new FileSystemResource(path.toFile());
            String ext = filename.contains(".") ? filename.substring(filename.lastIndexOf('.') + 1).toLowerCase() : "";
            MediaType type = MediaType.IMAGE_PNG;
            if ("jpg".equals(ext) || "jpeg".equals(ext)) type = MediaType.IMAGE_JPEG;
            else if ("gif".equals(ext)) type = MediaType.IMAGE_GIF;
            return ResponseEntity.ok()
                    .contentType(type)
                    .header("Cache-Control", "public, max-age=31536000")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "server_error"));
        }
    }

    @PutMapping("/me/authorizations")
    public ResponseEntity<?> updateAuthorizations(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                  @RequestBody UpdateAuthorizationsRequest req) {
        var token = AuthController.extractBearerToken(authHeader);
        if (token == null) return ResponseEntity.status(401).body(Map.of("error", "missing_token"));
        try {
            if (userService.isTokenBlacklisted(token)) return ResponseEntity.status(401).body(Map.of("error", "token_revoked"));
            Claims claims = authController.verify(token);
            Long userId = userService.resolveUserIdFromClaims(claims).orElseThrow();
            UserView u = userService.updateAuthorizations(userId, req.getConsentHealthAnalysis(), req.getConsentFinanceAnalysis());
            return ResponseEntity.ok(Map.of(
                    "consentHealthAnalysis", u.getConsentHealthAnalysis(),
                    "consentFinanceAnalysis", u.getConsentFinanceAnalysis()
            ));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", "user_not_found"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_token"));
        }
    }

    @PutMapping("/me/devices")
    public ResponseEntity<?> updateDevices(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                           @RequestBody UpdateDevicesRequest req) {
        var token = AuthController.extractBearerToken(authHeader);
        if (token == null) return ResponseEntity.status(401).body(Map.of("error", "missing_token"));
        try {
            if (userService.isTokenBlacklisted(token)) return ResponseEntity.status(401).body(Map.of("error", "token_revoked"));
            Claims claims = authController.verify(token);
            Long userId = userService.resolveUserIdFromClaims(claims).orElseThrow();
            var devices = userService.updateDevices(userId, req.getDevices());
            return ResponseEntity.ok(devices);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", "user_not_found"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_token"));
        }
    }

    @GetMapping("/me/persona")
    public ResponseEntity<?> getPersona(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        var token = AuthController.extractBearerToken(authHeader);
        if (token == null) return ResponseEntity.status(401).body(Map.of("error", "missing_token"));
        try {
            if (userService.isTokenBlacklisted(token)) return ResponseEntity.status(401).body(Map.of("error", "token_revoked"));
            Claims claims = authController.verify(token);
            Long userId = userService.resolveUserIdFromClaims(claims).orElseThrow();
            return ResponseEntity.ok(userService.getPersona(userId));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_token"));
        }
    }

    @PutMapping("/me/persona")
    public ResponseEntity<?> updatePersona(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                           @RequestBody PersonaUpdateRequest req) {
        var token = AuthController.extractBearerToken(authHeader);
        if (token == null) return ResponseEntity.status(401).body(Map.of("error", "missing_token"));
        try {
            if (userService.isTokenBlacklisted(token)) return ResponseEntity.status(401).body(Map.of("error", "token_revoked"));
            Claims claims = authController.verify(token);
            Long userId = Long.parseLong(claims.getSubject());
            return ResponseEntity.ok(userService.updatePersona(userId, req.getPersona()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", "user_not_found"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_token"));
        }
    }

    private Map<String, Object> sanitize(UserView u) {
        Map<String, Object> m = new java.util.HashMap<>();
        m.put("id", String.valueOf(u.getId()));
        m.put("username", u.getUsername());
        m.put("nickname", u.getNickname());
        m.put("avatarUrl", u.getAvatarUrl());
        m.put("email", u.getEmail());
        m.put("consentHealthAnalysis", u.getConsentHealthAnalysis());
        m.put("consentFinanceAnalysis", u.getConsentFinanceAnalysis());
        return m;
    }
}