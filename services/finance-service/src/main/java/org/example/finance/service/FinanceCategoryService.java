// FinanceCategoryService.java
package org.example.finance.service;

import org.example.finance.entity.FinanceCategory;
import java.util.List;

public interface FinanceCategoryService {
    List<FinanceCategory> getUserCategories(Long userId);
    FinanceCategory getCategoryById(Long id);
    FinanceCategory createCategory(FinanceCategory category);
    FinanceCategory updateCategory(FinanceCategory category);
    void deleteCategory(Long id);
    List<FinanceCategory> getCategoryTree(Long userId);
}