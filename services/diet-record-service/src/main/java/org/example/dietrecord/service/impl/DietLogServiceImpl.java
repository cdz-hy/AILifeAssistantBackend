package org.example.dietrecord.service.impl;

import org.example.dietrecord.dto.DailyNutritionStatsDTO;
import org.example.dietrecord.dto.DietEntryCreateRequest;
import org.example.dietrecord.dto.DietEntryDTO;
import org.example.dietrecord.dto.DietLogDTO;
import org.example.dietrecord.entity.DietEntry;
import org.example.dietrecord.entity.DietLog;
import org.example.dietrecord.entity.FoodItem;
import org.example.dietrecord.mapper.DietEntryMapper;
import org.example.dietrecord.mapper.DietLogMapper;
import org.example.dietrecord.service.DietLogService;
import org.example.dietrecord.service.FoodItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DietLogServiceImpl implements DietLogService {

    private final DietLogMapper dietLogMapper;
    private final DietEntryMapper dietEntryMapper;
    private final FoodItemService foodItemService;

    public DietLogServiceImpl(DietLogMapper dietLogMapper, 
                             DietEntryMapper dietEntryMapper,
                             FoodItemService foodItemService) {
        this.dietLogMapper = dietLogMapper;
        this.dietEntryMapper = dietEntryMapper;
        this.foodItemService = foodItemService;
    }

    @Override
    public DietLogDTO getDietLogByDate(Long userId, LocalDate date) {
        System.out.println("=== 开始查询饮食日志 - userId: " + userId + ", date: " + date + " ===");
        
        // 获取该日期的所有餐次
        List<DietLog> mealLogs = dietLogMapper.findByUserIdAndDate(userId, date);
        System.out.println("获取餐次记录数量: " + mealLogs.size());
        mealLogs.forEach(log -> System.out.println("  餐次ID: " + log.getId() + ", mealType: '" + log.getMealType() + "'"));
        
        // 获取该日期的所有饮食条目
        // 注意：在同一个事务中，MyBatis 一级缓存可能导致查询不到刚插入的数据
        // 这里直接查询数据库，不使用缓存
        List<DietEntry> entries = dietEntryMapper.findByUserIdAndDate(userId, date);
        System.out.println("获取饮食条目数量: " + entries.size());
        entries.forEach(entry -> System.out.println("  条目ID: " + entry.getId() + ", logId: " + entry.getLogId()));
        
        // 如果查询结果为空或数量不对，可能是缓存问题，尝试强制刷新
        if (entries.isEmpty()) {
            System.out.println("警告: 查询结果为空，可能是缓存问题");
        }
        
        // 将条目转换为 DTO，并关联餐次信息
        List<DietEntryDTO> entryDTOs = new ArrayList<>();
        Map<Long, DietLog> logMap = mealLogs.stream()
                .collect(Collectors.toMap(DietLog::getId, log -> log));
        
        for (DietEntry entry : entries) {
            DietLog mealLog = logMap.get(entry.getLogId());
            if (mealLog == null) {
                // 如果找不到对应的餐次记录，尝试从数据库查询
                System.out.println("警告: logMap中找不到logId=" + entry.getLogId() + "的餐次记录，尝试从数据库查询");
                mealLog = dietLogMapper.findById(entry.getLogId());
                if (mealLog != null) {
                    System.out.println("从数据库查询到餐次记录: ID=" + mealLog.getId() + ", mealType=" + mealLog.getMealType());
                    logMap.put(mealLog.getId(), mealLog);
                } else {
                    System.err.println("警告: 找不到餐次记录，entryId=" + entry.getId() + ", logId=" + entry.getLogId());
                }
            } else {
                System.out.println("找到餐次记录: entryId=" + entry.getId() + ", logId=" + entry.getLogId() + ", mealType=" + mealLog.getMealType());
            }
            DietEntryDTO dto = convertToDTO(entry, mealLog);
            System.out.println("转换后的DTO: entryId=" + dto.getId() + ", mealType=" + dto.getMealType());
            entryDTOs.add(dto);
        }

        // 获取营养统计
        DailyNutritionStatsDTO stats = getDailyNutritionStats(userId, date);

        DietLogDTO dto = new DietLogDTO();
        dto.setUserId(userId);
        dto.setLogDate(date);
        dto.setEntries(entryDTOs);
        dto.setNutritionStats(stats);

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DietLogDTO createDietEntry(Long userId, LocalDate date, DietEntryCreateRequest request) {
        System.out.println("创建饮食条目 - 用户ID: " + userId + ", 日期: " + date + ", 餐次类型: " + request.getMealType());
        
        // 查找或创建餐次
        String requestedMealType = request.getMealType();
        System.out.println("查询餐次记录 - userId: " + userId + ", date: " + date + ", mealType: '" + requestedMealType + "'");
        
        // 调试：先查询所有餐次记录
        List<DietLog> allMealLogsDebug = dietLogMapper.findAllByUserIdAndDate(userId, date);
        System.out.println("该日期所有餐次记录数量: " + allMealLogsDebug.size());
        allMealLogsDebug.forEach(log -> System.out.println("  餐次ID: " + log.getId() + ", mealType: '" + log.getMealType() + "'"));
        
        DietLog mealLog = dietLogMapper.findByUserIdAndDateAndMealType(userId, date, requestedMealType);
        final DietLog finalMealLog;
        
        if (mealLog == null) {
            System.out.println("未找到餐次记录，创建新餐次记录 - mealType: '" + requestedMealType + "'");
            DietLog newMealLog = new DietLog();
            newMealLog.setUserId(userId);
            newMealLog.setMealType(requestedMealType);
            newMealLog.setLogTime(LocalDateTime.now());
            int insertResult = dietLogMapper.insert(newMealLog);
            System.out.println("餐次记录插入结果: " + insertResult + ", 生成的ID: " + newMealLog.getId() + ", mealType: '" + newMealLog.getMealType() + "'");
            
            // 验证插入后的数据
            if (newMealLog.getId() != null) {
                DietLog verifyLog = dietLogMapper.findById(newMealLog.getId());
                if (verifyLog != null) {
                    System.out.println("验证插入的餐次记录 - ID: " + verifyLog.getId() + ", mealType: '" + verifyLog.getMealType() + "'");
                    if (!requestedMealType.equals(verifyLog.getMealType())) {
                        System.err.println("错误：插入的mealType不匹配！期望: '" + requestedMealType + "', 实际: '" + verifyLog.getMealType() + "'");
                    }
                }
            }
            finalMealLog = newMealLog;
        } else {
            System.out.println("找到现有餐次记录 - ID: " + mealLog.getId() + ", mealType: '" + mealLog.getMealType() + "'");
            if (!requestedMealType.equals(mealLog.getMealType())) {
                System.err.println("错误：查询返回的mealType不匹配！期望: '" + requestedMealType + "', 实际: '" + mealLog.getMealType() + "'");
                // 如果查询返回了错误的mealLog，强制创建新的
                System.out.println("强制创建新餐次记录");
                DietLog newMealLog = new DietLog();
                newMealLog.setUserId(userId);
                newMealLog.setMealType(requestedMealType);
                newMealLog.setLogTime(LocalDateTime.now());
                dietLogMapper.insert(newMealLog);
                System.out.println("强制创建餐次记录成功 - ID: " + newMealLog.getId() + ", mealType: '" + newMealLog.getMealType() + "'");
                finalMealLog = newMealLog;
            } else {
                finalMealLog = mealLog;
            }
        }

        // 处理食物信息
        Long foodItemId = null;
        String customFoodName = null;
        BigDecimal caloriesKcal = BigDecimal.ZERO;

        System.out.println("创建饮食条目 - 请求数据: foodItemId=" + request.getFoodItemId() + 
                ", foodName=" + request.getFoodName() + ", customFoodName=" + request.getCustomFoodName());

        if (request.getFoodItemId() != null) {
            // 如果提供了 foodItemId，直接使用
            foodItemId = request.getFoodItemId();
            FoodItem foodItem = foodItemService.getFoodItemById(foodItemId);
            System.out.println("使用 foodItemId 查找食物 - foodItemId: " + foodItemId + 
                    ", 找到食物: " + (foodItem != null ? foodItem.getFoodName() : "null"));
            if (foodItem != null) {
                System.out.println("食物营养成分 - 蛋白质: " + foodItem.getProteinPer100g() + 
                        ", 脂肪: " + foodItem.getFatPer100g() + ", 碳水: " + foodItem.getCarbsPer100g());
            }
            if (foodItem != null && request.getQuantityG() != null) {
                // 计算卡路里：每100克的卡路里 * 重量(克) / 100
                caloriesKcal = foodItem.getCaloriesPer100g()
                        .multiply(request.getQuantityG())
                        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            }
        } else if (request.getFoodName() != null && !request.getFoodName().trim().isEmpty()) {
            // 尝试从食物库查找
            FoodItem foodItem = foodItemService.getFoodItemByName(request.getFoodName());
            if (foodItem != null) {
                foodItemId = foodItem.getId();
                System.out.println("找到食物库中的食物 - foodItemId: " + foodItemId + ", 食物名称: " + foodItem.getFoodName());
                System.out.println("营养成分 - 蛋白质: " + foodItem.getProteinPer100g() + 
                        ", 脂肪: " + foodItem.getFatPer100g() + ", 碳水: " + foodItem.getCarbsPer100g());
                if (request.getQuantityG() != null) {
                    caloriesKcal = foodItem.getCaloriesPer100g()
                            .multiply(request.getQuantityG())
                            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                }
            } else {
                // 食物库中没有，使用自定义名称
                customFoodName = request.getFoodName();
                System.out.println("警告: 食物库中没有找到食物 '" + request.getFoodName() + "', 将使用自定义名称");
                // 如果前端提供了卡路里信息（从AI识别结果），可以根据卡路里估算营养成分
                // 这里暂时设为0，营养成分会在前端根据AI返回的数据计算
            }
        } else if (request.getCustomFoodName() != null && !request.getCustomFoodName().trim().isEmpty()) {
            customFoodName = request.getCustomFoodName();
        }

        // 处理图片 URL：如果是 base64 数据（data:image开头），不存储到数据库
        // 因为 base64 数据太长，应该上传到文件服务器后只存储 URL
        String imageUrl = request.getImageUrl();
        if (imageUrl != null && imageUrl.startsWith("data:image")) {
            // base64 数据不存储到数据库，设置为 null
            // 实际应用中应该先上传到文件服务器，然后存储 URL
            imageUrl = null;
        }
        // 如果 URL 太长（超过 500 字符），也截断或设置为 null
        if (imageUrl != null && imageUrl.length() > 500) {
            imageUrl = null;
        }

        // 创建饮食条目
        DietEntry entry = new DietEntry();
        entry.setLogId(finalMealLog.getId());
        entry.setImageUrl(imageUrl);
        entry.setFoodItemId(foodItemId);
        entry.setCustomFoodName(customFoodName);
        entry.setQuantityG(request.getQuantityG());
        entry.setQuantityDesc(request.getQuantityDesc());
        entry.setCaloriesKcal(caloriesKcal);

        System.out.println("准备创建饮食条目 - logId: " + entry.getLogId() + 
                ", mealLog.mealType: " + finalMealLog.getMealType() + 
                ", foodItemId: " + foodItemId + 
                ", customFoodName: " + customFoodName);
        dietEntryMapper.insert(entry);
        System.out.println("饮食条目创建成功 - entryId: " + entry.getId() + 
                ", logId: " + entry.getLogId() + 
                ", foodItemId: " + entry.getFoodItemId());
        
        // 验证 mealLog 的 mealType
        DietLog verifyMealLog = dietLogMapper.findById(finalMealLog.getId());
        System.out.println("验证餐次记录 - ID: " + verifyMealLog.getId() + ", mealType: " + verifyMealLog.getMealType());

        // 发布饮食日志事件到消息队列（简化版本，只记录日志）
        System.out.println("饮食日志事件发布（模拟）: 用户=" + userId + ", 日期=" + date + 
                ", 餐次=" + request.getMealType() + ", 食物=" + 
                (foodItemId != null ? "ID:" + foodItemId : customFoodName));

        // 刷新 MyBatis 缓存，确保能查询到刚插入的数据
        // 在同一个事务中，MyBatis 一级缓存可能导致查询不到刚插入的数据
        // 直接查询刚创建的 entry 和 mealLog，确保数据正确
        
        // 重新获取数据，确保包含刚创建的条目
        // 注意：在同一个事务中，MyBatis 一级缓存可能导致查询不到刚插入的数据
        // 解决方案：直接构建返回数据，而不是重新查询
        System.out.println("准备构建返回数据 - entryId: " + entry.getId() + ", logId: " + entry.getLogId());
        
        // 先验证刚创建的 entry 和 mealLog
        DietEntry verifyEntry = dietEntryMapper.findById(entry.getId());
        if (verifyEntry != null) {
            System.out.println("验证刚创建的entry - entryId: " + verifyEntry.getId() + ", logId: " + verifyEntry.getLogId());
            DietLog verifyMealLog2 = dietLogMapper.findById(verifyEntry.getLogId());
            if (verifyMealLog2 != null) {
                System.out.println("验证entry关联的mealLog - logId: " + verifyMealLog2.getId() + ", mealType: '" + verifyMealLog2.getMealType() + "'");
            }
        }
        
        // 问题：在同一个事务中，MyBatis 一级缓存可能导致查询不到刚插入的数据
        // 解决方案：直接构建返回数据，包含刚创建的条目，而不是重新查询
        System.out.println("=== 直接构建返回数据，包含刚创建的条目 ===");
        
        // 获取该日期的所有餐次（包括刚创建的）
        List<DietLog> mealLogsForReturn = dietLogMapper.findAllByUserIdAndDate(userId, date);
        System.out.println("所有餐次记录数量: " + mealLogsForReturn.size());
        mealLogsForReturn.forEach(log -> System.out.println("  餐次ID: " + log.getId() + ", mealType: '" + log.getMealType() + "'"));
        
        // 获取该日期的所有饮食条目（包括刚创建的）
        List<DietEntry> entriesForReturn = dietEntryMapper.findByUserIdAndDate(userId, date);
        System.out.println("所有饮食条目数量: " + entriesForReturn.size());
        entriesForReturn.forEach(e -> System.out.println("  条目ID: " + e.getId() + ", logId: " + e.getLogId()));
        
        // 如果查询结果不包含刚创建的条目，手动添加
        boolean foundNewEntry = entriesForReturn.stream().anyMatch(e -> e.getId().equals(entry.getId()));
        if (!foundNewEntry) {
            System.out.println("警告: 查询结果不包含刚创建的条目，手动添加 - entryId: " + entry.getId());
            entriesForReturn.add(entry);
        }
        
        // 如果查询结果不包含刚创建的 mealLog，手动添加
        boolean foundNewMealLog = mealLogsForReturn.stream().anyMatch(m -> m.getId().equals(finalMealLog.getId()));
        if (!foundNewMealLog) {
            System.out.println("警告: 查询结果不包含刚创建的餐次，手动添加 - mealLogId: " + finalMealLog.getId());
            mealLogsForReturn.add(finalMealLog);
        }
        
        // 构建返回数据
        List<DietEntryDTO> entryDTOs = new ArrayList<>();
        Map<Long, DietLog> logMap = mealLogsForReturn.stream()
                .collect(Collectors.toMap(DietLog::getId, log -> log));
        
        for (DietEntry e : entriesForReturn) {
            DietLog ml = logMap.get(e.getLogId());
            if (ml == null) {
                ml = dietLogMapper.findById(e.getLogId());
                if (ml != null) {
                    logMap.put(ml.getId(), ml);
                }
            }
            DietEntryDTO dto = convertToDTO(e, ml);
            entryDTOs.add(dto);
        }
        
        DailyNutritionStatsDTO stats = getDailyNutritionStats(userId, date);
        
        DietLogDTO result = new DietLogDTO();
        result.setUserId(userId);
        result.setLogDate(date);
        result.setEntries(entryDTOs);
        result.setNutritionStats(stats);
        
        System.out.println("返回的饮食日志 - 条目数量: " + (result.getEntries() != null ? result.getEntries().size() : 0));
        if (result.getEntries() != null) {
            result.getEntries().forEach(e -> System.out.println("  返回条目: id=" + e.getId() + ", mealType='" + e.getMealType() + "'"));
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteDietEntry(Long userId, Long entryId) {
        // 验证条目是否属于该用户
        DietEntry entry = dietEntryMapper.findById(entryId);
        if (entry == null) {
            throw new RuntimeException("饮食条目不存在");
        }

        DietLog mealLog = dietLogMapper.findById(entry.getLogId());
        if (mealLog == null || !mealLog.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此饮食条目");
        }

        dietEntryMapper.delete(entryId);
    }

    @Override
    public DailyNutritionStatsDTO getDailyNutritionStats(Long userId, LocalDate date) {
        DailyNutritionStatsDTO stats = new DailyNutritionStatsDTO();

        BigDecimal totalCalories = dietEntryMapper.getTotalCaloriesByDate(userId, date);
        BigDecimal totalProtein = dietEntryMapper.getTotalProteinByDate(userId, date);
        BigDecimal totalFat = dietEntryMapper.getTotalFatByDate(userId, date);
        BigDecimal totalCarbs = dietEntryMapper.getTotalCarbsByDate(userId, date);

        stats.setTotalCalories(totalCalories != null ? totalCalories : BigDecimal.ZERO);
        stats.setTotalProtein(totalProtein != null ? totalProtein : BigDecimal.ZERO);
        stats.setTotalFat(totalFat != null ? totalFat : BigDecimal.ZERO);
        stats.setTotalCarbs(totalCarbs != null ? totalCarbs : BigDecimal.ZERO);

        // 设置目标值（可以根据用户配置设置，这里使用默认值）
        stats.setTargetCalories(new BigDecimal("2200"));
        stats.setTargetProtein(new BigDecimal("100"));
        stats.setTargetFat(new BigDecimal("60"));
        stats.setTargetCarbs(new BigDecimal("250"));

        return stats;
    }

    @Override
    public List<DietEntryDTO> getDietEntriesByMealType(Long userId, LocalDate date, String mealType) {
        List<DietEntry> entries = dietEntryMapper.findByUserIdAndDateAndMealType(userId, date, mealType);
        List<DietLog> mealLogs = dietLogMapper.findByUserIdAndDate(userId, date);
        Map<Long, DietLog> logMap = mealLogs.stream()
                .collect(Collectors.toMap(DietLog::getId, log -> log));
        
        return entries.stream()
                .map(entry -> convertToDTO(entry, logMap.get(entry.getLogId())))
                .collect(Collectors.toList());
    }

    private DietEntryDTO convertToDTO(DietEntry entry, DietLog mealLog) {
        DietEntryDTO dto = new DietEntryDTO();
        dto.setId(entry.getId());
        dto.setLogId(entry.getLogId());
        String mealType = mealLog != null ? mealLog.getMealType() : null;
        if (mealType == null) {
            System.err.println("警告: entryId=" + entry.getId() + ", logId=" + entry.getLogId() + " 的 mealType 为 null");
        }
        dto.setMealType(mealType);
        dto.setQuantityG(entry.getQuantityG());
        dto.setQuantityDesc(entry.getQuantityDesc());
        dto.setCaloriesKcal(entry.getCaloriesKcal());
        dto.setImageUrl(entry.getImageUrl());

        // 获取食物名称
        if (entry.getFoodItemId() != null) {
            FoodItem foodItem = foodItemService.getFoodItemById(entry.getFoodItemId());
            dto.setFoodName(foodItem != null ? foodItem.getFoodName() : null);
            
            // 计算营养成分（从食物库）
            if (foodItem != null && entry.getQuantityG() != null) {
                BigDecimal ratio = entry.getQuantityG().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                dto.setProtein(foodItem.getProteinPer100g() != null ? 
                        foodItem.getProteinPer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                dto.setFat(foodItem.getFatPer100g() != null ? 
                        foodItem.getFatPer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                dto.setCarbs(foodItem.getCarbsPer100g() != null ? 
                        foodItem.getCarbsPer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            } else {
                dto.setProtein(BigDecimal.ZERO);
                dto.setFat(BigDecimal.ZERO);
                dto.setCarbs(BigDecimal.ZERO);
            }
        } else {
            dto.setFoodName(entry.getCustomFoodName());
            // 自定义食物没有营养成分数据
            dto.setProtein(BigDecimal.ZERO);
            dto.setFat(BigDecimal.ZERO);
            dto.setCarbs(BigDecimal.ZERO);
        }

        return dto;
    }
}
