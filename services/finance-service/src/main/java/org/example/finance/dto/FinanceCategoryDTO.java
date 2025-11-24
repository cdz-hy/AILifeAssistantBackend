package org.example.finance.dto;

import java.util.List;

public class FinanceCategoryDTO {
    private Long id;
    private Long parentId;
    private String categoryName;
    private List<FinanceCategoryDTO> children;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<FinanceCategoryDTO> getChildren() {
        return children;
    }

    public void setChildren(List<FinanceCategoryDTO> children) {
        this.children = children;
    }
}