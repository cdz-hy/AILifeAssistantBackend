package org.example.finance.service.impl;

import org.example.finance.entity.Budget;
import org.example.finance.mapper.BudgetMapper;
import org.example.finance.mapper.TransactionMapper;
import org.example.finance.service.BudgetService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetMapper budgetMapper;
    private final TransactionMapper transactionMapper;

    public BudgetServiceImpl(BudgetMapper budgetMapper, TransactionMapper transactionMapper) {
        this.budgetMapper = budgetMapper;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public List<Budget> getUserBudgets(Long userId, Integer year, Integer month) {
        List<Budget> budgets = budgetMapper.findByUserIdAndMonth(userId, year, month);

        // 计算每个预算的当前支出和剩余金额
        for (Budget budget : budgets) {
            BigDecimal currentSpending;
            // 如果categoryId为null，表示总预算，使用总支出统计
            if (budget.getCategoryId() == null) {
                currentSpending = transactionMapper.getMonthlyExpense(userId, year, month);
            } else {
                currentSpending = transactionMapper.getMonthlyExpenseByCategory(
                        userId, budget.getCategoryId(), year, month);
            }
            budget.setCurrentSpending(currentSpending != null ? currentSpending : BigDecimal.ZERO);

            BigDecimal currentSpendingValue = budget.getCurrentSpending() != null ? budget.getCurrentSpending() : BigDecimal.ZERO;
            budget.setRemainingAmount(budget.getAmount().subtract(currentSpendingValue));
        }

        return budgets;
    }

    @Override
    public Budget getBudgetById(Long id) {
        return budgetMapper.findById(id);
    }

    @Override
    public Budget createBudget(Budget budget) {
        // 检查是否已存在相同月份和分类的预算
        Budget existing = budgetMapper.findByUserCategoryAndMonth(
                budget.getUserId(), budget.getCategoryId(),
                budget.getBudgetYear(), budget.getBudgetMonth());

        if (existing != null) {
            throw new RuntimeException("该分类在本月已设置预算");
        }

        budgetMapper.insert(budget);
        return budgetMapper.findById(budget.getId());
    }

    @Override
    public Budget updateBudget(Budget budget) {
        budgetMapper.update(budget);

        // 检查预算是否超支（简化版本，只记录日志）
        checkBudgetOverspending(budget.getUserId(), budget.getBudgetYear(), budget.getBudgetMonth());

        return budgetMapper.findById(budget.getId());
    }

    @Override
    public void deleteBudget(Long id) {
        budgetMapper.delete(id);
    }

    @Override
    public void checkBudgetOverspending(Long userId, Integer year, Integer month) {
        List<Budget> budgets = getUserBudgets(userId, year, month);

        for (Budget budget : budgets) {
            if (budget.getCurrentSpending().compareTo(budget.getAmount()) > 0) {
                // 简化版本：只记录日志，不发送消息队列
                System.out.println("预算超支预警 - 用户: " + userId +
                        ", 分类: " + budget.getCategoryName() +
                        ", 预算: " + budget.getAmount() +
                        ", 实际支出: " + budget.getCurrentSpending());
            }
        }
    }
}