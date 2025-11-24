package org.example.user.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class User {
    private final String id;
    private String username;
    private String passwordHash;
    private String nickname;
    private String avatarUrl;
    private Authorizations authorizations = new Authorizations();
    private List<String> devices = new ArrayList<>();
    private Map<String, Object> persona = new HashMap<>();

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Authorizations getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(Authorizations authorizations) {
        this.authorizations = authorizations;
    }

    public List<String> getDevices() {
        return devices;
    }

    public void setDevices(List<String> devices) {
        this.devices = devices;
    }

    public Map<String, Object> getPersona() {
        return persona;
    }

    public void setPersona(Map<String, Object> persona) {
        this.persona = persona;
    }
}