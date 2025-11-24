package org.example.user.dto;

public class UpdateAuthorizationsRequest {
    private Boolean consentHealthAnalysis;
    private Boolean consentFinanceAnalysis;

    public Boolean getConsentHealthAnalysis() { return consentHealthAnalysis; }
    public void setConsentHealthAnalysis(Boolean consentHealthAnalysis) { this.consentHealthAnalysis = consentHealthAnalysis; }
    public Boolean getConsentFinanceAnalysis() { return consentFinanceAnalysis; }
    public void setConsentFinanceAnalysis(Boolean consentFinanceAnalysis) { this.consentFinanceAnalysis = consentFinanceAnalysis; }
}