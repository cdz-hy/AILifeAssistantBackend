package org.example.finance.controller;

import org.example.finance.entity.Budget;
import org.example.finance.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/finance/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public ResponseEntity<List<Budget>> getUserBudgets(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        try {
            List<Budget> budgets = budgetService.getUserBudgets(userId, year, month);
            return ResponseEntity.ok(budgets);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable Long id) {
        try {
            Budget budget = budgetService.getBudgetById(id);
            if (budget != null) {
                return ResponseEntity.ok(budget);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Budget> createBudget(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody Budget budget) {
        try {
            budget.setUserId(userId);
            Budget createdBudget = budgetService.createBudget(budget);
            return ResponseEntity.ok(createdBudget);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @RequestBody Budget budget) {
        try {
            budget.setId(id);
            budget.setUserId(userId);
            Budget updatedBudget = budgetService.updateBudget(budget);
            return ResponseEntity.ok(updatedBudget);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        try {
            budgetService.deleteBudget(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/check-overspending")
    public ResponseEntity<Void> checkBudgetOverspending(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {
        try {
            Budget budget = budgetService.getBudgetById(id);
            if (budget != null) {
                budgetService.checkBudgetOverspending(
                        budget.getUserId(),
                        budget.getBudgetYear(),
                        budget.getBudgetMonth()
                );
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}