package org.example.user.dto;

import java.util.List;

public class UpdateDevicesRequest {
    private List<String> devices;

    public List<String> getDevices() { return devices; }
    public void setDevices(List<String> devices) { this.devices = devices; }
}