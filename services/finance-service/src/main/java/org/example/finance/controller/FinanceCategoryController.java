package org.example.finance.controller;

import org.example.finance.entity.FinanceCategory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/finance/categories")
public class FinanceCategoryController {

    @GetMapping
    public List<FinanceCategory> getUserCategories(@RequestHeader("X-User-Id") Long userId) {
        // 临时返回空列表，后续实现业务逻辑
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public FinanceCategory getCategoryById(@PathVariable Long id) {
        // 临时返回空对象，后续实现业务逻辑
        return new FinanceCategory();
    }

    @PostMapping
    public FinanceCategory createCategory(@RequestHeader("X-User-Id") Long userId,
                                          @RequestBody FinanceCategory category) {
        // 临时返回传入的对象，后续实现业务逻辑
        return category;
    }
}