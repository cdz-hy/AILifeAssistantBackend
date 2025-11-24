package org.example.user.view;

public class UserView {
    private Long id;
    private String username;
    private String nickname;
    private String avatarUrl;
    private Boolean consentHealthAnalysis;
    private Boolean consentFinanceAnalysis;
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public Boolean getConsentHealthAnalysis() { return consentHealthAnalysis; }
    public void setConsentHealthAnalysis(Boolean consentHealthAnalysis) { this.consentHealthAnalysis = consentHealthAnalysis; }
    public Boolean getConsentFinanceAnalysis() { return consentFinanceAnalysis; }
    public void setConsentFinanceAnalysis(Boolean consentFinanceAnalysis) { this.consentFinanceAnalysis = consentFinanceAnalysis; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}