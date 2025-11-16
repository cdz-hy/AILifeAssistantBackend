package org.example.finance.service;

import org.example.finance.entity.Budget;
import java.util.List;

public interface BudgetService {
    List<Budget> getUserBudgets(Long userId, Integer year, Integer month);
    Budget getBudgetById(Long id);
    Budget createBudget(Budget budget);
    Budget updateBudget(Budget budget);
    void deleteBudget(Long id);
    void checkBudgetOverspending(Long userId, Integer year, Integer month);
}