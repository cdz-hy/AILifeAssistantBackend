package org.example.dietrecord.controller;

import org.example.dietrecord.dto.DailyNutritionStatsDTO;
import org.example.dietrecord.dto.DietEntryCreateRequest;
import org.example.dietrecord.dto.DietEntryDTO;
import org.example.dietrecord.dto.DietLogDTO;
import org.example.dietrecord.service.DietLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diet-logs")
public class DietLogController {

    private final DietLogService dietLogService;

    public DietLogController(DietLogService dietLogService) {
        this.dietLogService = dietLogService;
    }

    @GetMapping("/{userId}/{date}")
    public ResponseEntity<DietLogDTO> getDietLog(
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            Long actualUserId = headerUserId != null ? headerUserId : userId;
            DietLogDTO dietLog = dietLogService.getDietLogByDate(actualUserId, date);
            return ResponseEntity.ok(dietLog);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{userId}/{date}/entries")
    public ResponseEntity<?> createDietEntry(
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody DietEntryCreateRequest request) {
        try {
            // 验证请求数据
            if (request.getMealType() == null || request.getMealType().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "餐次类型不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            if (request.getFoodItemId() == null && 
                (request.getFoodName() == null || request.getFoodName().trim().isEmpty()) &&
                (request.getCustomFoodName() == null || request.getCustomFoodName().trim().isEmpty())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "食物信息不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            Long actualUserId = headerUserId != null ? headerUserId : userId;
            DietLogDTO dietLog = dietLogService.createDietEntry(actualUserId, date, request);
            return ResponseEntity.ok(dietLog);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            String errorMessage = e.getMessage() != null ? e.getMessage() : "创建饮食条目失败";
            error.put("error", errorMessage);
            error.put("message", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/entries/{entryId}")
    public ResponseEntity<Void> deleteDietEntry(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @PathVariable Long entryId) {
        try {
            if (userId == null) {
                return ResponseEntity.badRequest().build();
            }
            dietLogService.deleteDietEntry(userId, entryId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{userId}/{date}/stats")
    public ResponseEntity<DailyNutritionStatsDTO> getDailyNutritionStats(
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            Long actualUserId = headerUserId != null ? headerUserId : userId;
            DailyNutritionStatsDTO stats = dietLogService.getDailyNutritionStats(actualUserId, date);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{userId}/{date}/entries")
    public ResponseEntity<List<DietEntryDTO>> getDietEntriesByMealType(
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String mealType) {
        try {
            Long actualUserId = headerUserId != null ? headerUserId : userId;
            List<DietEntryDTO> entries;
            if (mealType != null && !mealType.isEmpty()) {
                entries = dietLogService.getDietEntriesByMealType(actualUserId, date, mealType);
            } else {
                DietLogDTO dietLog = dietLogService.getDietLogByDate(actualUserId, date);
                entries = dietLog.getEntries();
            }
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}

