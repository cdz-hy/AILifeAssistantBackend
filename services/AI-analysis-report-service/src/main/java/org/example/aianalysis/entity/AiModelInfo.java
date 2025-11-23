package org.example.aianalysis.entity;

/**
 * AI模型信息实体类
 * 对应数据库表 t_ai_model_info
 */
public class AiModelInfo {
    private Long id;
    private String modelName;
    private String version;
    private String description;

    // Constructors
    public AiModelInfo() {}

    public AiModelInfo(Long id, String modelName, String version, String description) {
        this.id = id;
        this.modelName = modelName;
        this.version = version;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}