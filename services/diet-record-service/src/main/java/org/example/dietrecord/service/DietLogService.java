package org.example.dietrecord.service;

import org.example.dietrecord.dto.DailyNutritionStatsDTO;
import org.example.dietrecord.dto.DietEntryCreateRequest;
import org.example.dietrecord.dto.DietEntryDTO;
import org.example.dietrecord.dto.DietLogDTO;

import java.time.LocalDate;
import java.util.List;

public interface DietLogService {
    DietLogDTO getDietLogByDate(Long userId, LocalDate date);
    DietLogDTO createDietEntry(Long userId, LocalDate date, DietEntryCreateRequest request);
    void deleteDietEntry(Long userId, Long entryId);
    DailyNutritionStatsDTO getDailyNutritionStats(Long userId, LocalDate date);
    List<DietEntryDTO> getDietEntriesByMealType(Long userId, LocalDate date, String mealType);
}

