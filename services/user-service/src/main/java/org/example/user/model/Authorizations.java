package org.example.user.model;

public class Authorizations {
    private boolean consentHealthAnalysis;
    private boolean consentFinanceAnalysis;

    public boolean isConsentHealthAnalysis() {
        return consentHealthAnalysis;
    }

    public void setConsentHealthAnalysis(boolean consentHealthAnalysis) {
        this.consentHealthAnalysis = consentHealthAnalysis;
    }

    public boolean isConsentFinanceAnalysis() {
        return consentFinanceAnalysis;
    }

    public void setConsentFinanceAnalysis(boolean consentFinanceAnalysis) {
        this.consentFinanceAnalysis = consentFinanceAnalysis;
    }
}