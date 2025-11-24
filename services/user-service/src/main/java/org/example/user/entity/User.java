package org.example.user.entity;

public class User {
    private String id;
    private String username;
    private String passwordHash;
    private String nickname;
    private String avatarUrl;
    private Boolean consentHealthAnalysis;
    private Boolean consentFinanceAnalysis;
    private String personaJson;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public Boolean getConsentHealthAnalysis() { return consentHealthAnalysis; }
    public void setConsentHealthAnalysis(Boolean consentHealthAnalysis) { this.consentHealthAnalysis = consentHealthAnalysis; }
    public Boolean getConsentFinanceAnalysis() { return consentFinanceAnalysis; }
    public void setConsentFinanceAnalysis(Boolean consentFinanceAnalysis) { this.consentFinanceAnalysis = consentFinanceAnalysis; }
    public String getPersonaJson() { return personaJson; }
    public void setPersonaJson(String personaJson) { this.personaJson = personaJson; }
}