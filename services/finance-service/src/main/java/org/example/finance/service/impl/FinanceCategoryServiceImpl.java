package org.example.finance.service.impl;

import org.example.finance.entity.FinanceCategory;
import org.example.finance.mapper.FinanceCategoryMapper;
import org.example.finance.service.FinanceCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceCategoryServiceImpl implements FinanceCategoryService {

    private final FinanceCategoryMapper categoryMapper;

    public FinanceCategoryServiceImpl(FinanceCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<FinanceCategory> getUserCategories(Long userId) {
        return categoryMapper.findByUserId(userId);
    }

    @Override
    public List<FinanceCategory> getSystemCategories() {
        return categoryMapper.findSystemCategories();
    }

    @Override
    public FinanceCategory getCategoryById(Long id) {
        return categoryMapper.findById(id);
    }

    @Override
    public FinanceCategory createCategory(FinanceCategory category) {
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public FinanceCategory updateCategory(FinanceCategory category) {
        categoryMapper.update(category);
        return categoryMapper.findById(category.getId());
    }

    @Override
    public void deleteCategory(Long id) {
        categoryMapper.delete(id);
    }

    @Override
    public List<FinanceCategory> getCategoryTree(Long userId) {
        List<FinanceCategory> allCategories = categoryMapper.findByUserId(userId);
        return buildCategoryTree(allCategories);
    }

    private List<FinanceCategory> buildCategoryTree(List<FinanceCategory> categories) {
        List<FinanceCategory> rootCategories = categories.stream()
                .filter(category -> category.getParentId() == null)
                .collect(Collectors.toList());

        for (FinanceCategory root : rootCategories) {
            root.setChildren(getChildCategories(root.getId(), categories));
        }

        return rootCategories;
    }

    private List<FinanceCategory> getChildCategories(Long parentId, List<FinanceCategory> categories) {
        List<FinanceCategory> children = categories.stream()
                .filter(category -> parentId.equals(category.getParentId()))
                .collect(Collectors.toList());

        for (FinanceCategory child : children) {
            child.setChildren(getChildCategories(child.getId(), categories));
        }

        return children;
    }
}