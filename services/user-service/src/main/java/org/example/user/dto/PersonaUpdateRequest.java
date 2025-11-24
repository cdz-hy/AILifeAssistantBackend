package org.example.user.dto;

import java.util.Map;

public class PersonaUpdateRequest {
    private Map<String, Object> persona;

    public Map<String, Object> getPersona() { return persona; }
    public void setPersona(Map<String, Object> persona) { this.persona = persona; }
}