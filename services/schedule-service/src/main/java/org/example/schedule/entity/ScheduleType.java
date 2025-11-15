package org.example.schedule.entity;

public class ScheduleType {
    private Long id;
    private Long userId;
    private String typeName;
    private String colorHex;

    // Constructors
    public ScheduleType() {}

    public ScheduleType(Long id, Long userId, String typeName, String colorHex) {
        this.id = id;
        this.userId = userId;
        this.typeName = typeName;
        this.colorHex = colorHex;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
}