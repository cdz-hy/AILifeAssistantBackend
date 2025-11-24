package org.example.user.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.user.entity.UserCore;
import org.example.user.entity.UserProfile;
import org.example.user.mapper.UserCoreMapper;
import org.example.user.mapper.UserProfileMapper;
import org.example.user.mapper.BoundDeviceMapper;
import org.example.user.mapper.DataAuthorizationMapper;
import org.example.user.mapper.PersonaMetricMapper;
import org.example.user.view.UserView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserCoreMapper userCoreMapper;
    private final UserProfileMapper userProfileMapper;
    private final BoundDeviceMapper boundDeviceMapper;
    private final DataAuthorizationMapper dataAuthorizationMapper;
    private final PersonaMetricMapper personaMetricMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Set<String> tokenBlacklist = Collections.synchronizedSet(new HashSet<>());
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserService(UserCoreMapper userCoreMapper,
                       UserProfileMapper userProfileMapper,
                       BoundDeviceMapper boundDeviceMapper,
                       DataAuthorizationMapper dataAuthorizationMapper,
                       PersonaMetricMapper personaMetricMapper) {
        this.userCoreMapper = userCoreMapper;
        this.userProfileMapper = userProfileMapper;
        this.boundDeviceMapper = boundDeviceMapper;
        this.dataAuthorizationMapper = dataAuthorizationMapper;
        this.personaMetricMapper = personaMetricMapper;
    }

    public UserView register(String username, String rawPassword, String nickname) {
        if (userCoreMapper.findByUsername(username) != null) {
            throw new IllegalStateException("username_exists");
        }
        UserCore core = new UserCore();
        core.setUsername(username);
        core.setPasswordHash(passwordEncoder.encode(rawPassword));
        userCoreMapper.insert(core);
        Long userId = core.getId();
        UserProfile profile = new UserProfile();
        profile.setUserId(userId);
        profile.setNickname(nickname == null ? username : nickname);
        profile.setAvatarUrl(null);
        userProfileMapper.insert(profile);
        UserView view = new UserView();
        view.setId(userId);
        view.setUsername(username);
        view.setNickname(profile.getNickname());
        view.setAvatarUrl(profile.getAvatarUrl());
        view.setConsentHealthAnalysis(false);
        view.setConsentFinanceAnalysis(false);
        return view;
    }

    public Optional<UserView> validateCredentials(String username, String rawPassword) {
        UserCore core = userCoreMapper.findByUsername(username);
        if (core == null) return Optional.empty();
        if (!passwordEncoder.matches(rawPassword, core.getPasswordHash())) return Optional.empty();
        return findById(core.getId());
    }

    public Optional<UserView> findById(Long id) {
        UserCore core = userCoreMapper.findById(id);
        if (core == null) return Optional.empty();
        UserProfile profile = userProfileMapper.findByUserId(id);
        var auths = dataAuthorizationMapper.listByUserId(id);
        boolean consentHealth = auths.stream().anyMatch(a -> "health_analysis".equals(a.getDataType()) && Boolean.TRUE.equals(a.getIsAuthorized()));
        boolean consentFinance = auths.stream().anyMatch(a -> "finance_analysis".equals(a.getDataType()) && Boolean.TRUE.equals(a.getIsAuthorized()));
        UserView view = new UserView();
        view.setId(core.getId());
        view.setUsername(core.getUsername());
        view.setNickname(profile != null ? profile.getNickname() : core.getUsername());
        view.setAvatarUrl(profile != null ? profile.getAvatarUrl() : null);
        view.setEmail(core.getEmail());
        view.setConsentHealthAnalysis(consentHealth);
        view.setConsentFinanceAnalysis(consentFinance);
        return Optional.of(view);
    }

    public Optional<UserView> findByUsername(String username) {
        UserCore core = userCoreMapper.findByUsername(username);
        if (core == null) return Optional.empty();
        return findById(core.getId());
    }

    public Optional<Long> resolveUserIdFromClaims(io.jsonwebtoken.Claims claims) {
        try {
            return Optional.of(Long.parseLong(claims.getSubject()));
        } catch (Exception ignored) {}
        Object uname = claims.get("username");
        if (uname != null) {
            UserCore core = userCoreMapper.findByUsername(String.valueOf(uname));
            if (core != null) return Optional.of(core.getId());
        }
        return Optional.empty();
    }

    public UserView updateProfile(Long id, String nickname, String avatarUrl, String email) {
        UserProfile existing = userProfileMapper.findByUserId(id);
        if (existing == null) {
            UserProfile profile = new UserProfile();
            profile.setUserId(id);
            profile.setNickname(nickname);
            profile.setAvatarUrl(avatarUrl);
            userProfileMapper.insert(profile);
        } else {
            String nickToSet = nickname != null ? nickname : existing.getNickname();
            String avatarToSet = avatarUrl != null ? avatarUrl : existing.getAvatarUrl();
            userProfileMapper.update(id, nickToSet, avatarToSet);
        }
        if (email != null && !email.isBlank()) {
            try {
                userCoreMapper.updateEmail(id, email);
            } catch (Exception e) {
                throw new IllegalStateException("email_exists");
            }
        }
        return findById(id).orElseThrow();
    }

    public UserView updateAuthorizations(Long id, Boolean consentHealth, Boolean consentFinance) {
        if (consentHealth != null)
            dataAuthorizationMapper.upsert(id, "health_analysis", consentHealth);
        if (consentFinance != null)
            dataAuthorizationMapper.upsert(id, "finance_analysis", consentFinance);
        return findById(id).orElseThrow();
    }

    public List<String> getDevices(Long id) {
        return boundDeviceMapper.listDeviceTokens(id);
    }

    public List<String> updateDevices(Long id, List<String> devices) {
        boundDeviceMapper.deleteByUserId(id);
        if (devices != null) {
            for (String d : devices) {
                if (d != null && !d.isBlank()) {
                    boundDeviceMapper.insert(id, "web", d);
                }
            }
        }
        return boundDeviceMapper.listDeviceTokens(id);
    }

    public Map<String, Object> getPersona(Long id) {
        var list = personaMetricMapper.listByUserId(id);
        Map<String, Object> map = new HashMap<>();
        for (var pm : list) map.put(pm.getMetricName(), pm.getMetricValue());
        return map;
    }

    public Map<String, Object> updatePersona(Long id, Map<String, Object> persona) {
        if (persona != null) {
            for (var entry : persona.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue() == null ? "" : String.valueOf(entry.getValue());
                personaMetricMapper.upsert(id, name, value, "user_input");
            }
        }
        return getPersona(id);
    }

    public void blacklistToken(String token) {
        tokenBlacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}