package org.example.finance.entity;

import java.time.LocalDateTime;
import java.util.List;

public class FinanceCategory {
    private Long id;
    private Long parentId;
    private Long userId;
    private String categoryName;
    private LocalDateTime createdAt;
    private List<FinanceCategory> children;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<FinanceCategory> getChildren() { return children; }
    public void setChildren(List<FinanceCategory> children) { this.children = children; }
}