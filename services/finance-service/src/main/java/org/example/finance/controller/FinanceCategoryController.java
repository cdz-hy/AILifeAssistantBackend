package org.example.finance.controller;

import org.example.finance.dto.FinanceCategoryDTO;
import org.example.finance.entity.FinanceCategory;
import org.example.finance.service.FinanceCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance/categories")
public class FinanceCategoryController {

    private final FinanceCategoryService categoryService;

    public FinanceCategoryController(FinanceCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<FinanceCategoryDTO>> getUserCategories(@RequestHeader("X-User-Id") Long userId) {
        try {
            List<FinanceCategory> categories = categoryService.getUserCategories(userId);
            List<FinanceCategoryDTO> categoryDTOs = categories.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(categoryDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<FinanceCategoryDTO>> getAllCategories() {
        try {
            List<FinanceCategory> systemCategories = categoryService.getSystemCategories();
            List<FinanceCategoryDTO> categoryDTOs = systemCategories.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(categoryDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinanceCategoryDTO> getCategoryById(@PathVariable Long id) {
        try {
            FinanceCategory category = categoryService.getCategoryById(id);
            if (category != null) {
                return ResponseEntity.ok(convertToDTO(category));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<FinanceCategoryDTO> createCategory(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody FinanceCategory category) {
        try {
            category.setUserId(userId);
            FinanceCategory createdCategory = categoryService.createCategory(category);
            return ResponseEntity.ok(convertToDTO(createdCategory));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinanceCategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody FinanceCategory category) {
        try {
            category.setId(id);
            FinanceCategory updatedCategory = categoryService.updateCategory(category);
            return ResponseEntity.ok(convertToDTO(updatedCategory));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tree")
    public ResponseEntity<List<FinanceCategoryDTO>> getCategoryTree(@RequestHeader("X-User-Id") Long userId) {
        try {
            List<FinanceCategory> categoryTree = categoryService.getCategoryTree(userId);
            List<FinanceCategoryDTO> categoryDTOs = categoryTree.stream()
                    .map(this::convertToDTOWithChildren)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(categoryDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    private FinanceCategoryDTO convertToDTO(FinanceCategory category) {
        FinanceCategoryDTO dto = new FinanceCategoryDTO();
        dto.setId(category.getId());
        dto.setParentId(category.getParentId());
        dto.setCategoryName(category.getCategoryName());
        return dto;
    }

    private FinanceCategoryDTO convertToDTOWithChildren(FinanceCategory category) {
        FinanceCategoryDTO dto = convertToDTO(category);
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            List<FinanceCategoryDTO> childDTOs = category.getChildren().stream()
                    .map(this::convertToDTOWithChildren)
                    .collect(Collectors.toList());
            dto.setChildren(childDTOs);
        }
        return dto;
    }
}