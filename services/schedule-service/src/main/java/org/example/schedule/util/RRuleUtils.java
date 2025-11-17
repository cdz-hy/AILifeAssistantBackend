package org.example.schedule.util;

import org.example.schedule.entity.RecurrencePattern;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RRule工具类
 * 用于处理重复规则的生成和解析
 */
public class RRuleUtils {
    
    /**
     * 将RecurrencePattern转换为iCal RRULE字符串
     * @param pattern 重复模式
     * @return iCal RRULE字符串
     */
    public static String toRRule(RecurrencePattern pattern) {
        if (pattern == null) {
            return null;
        }
        
        StringBuilder rrule = new StringBuilder("RRULE:");
        
        switch (pattern.getFrequency()) {
            case ONCE:
                // 一次性事件不需要重复规则
                return null;
                
            case DAILY:
                rrule.append("FREQ=DAILY");
                if (pattern.getInterval() != null && pattern.getInterval() > 1) {
                    rrule.append(";INTERVAL=").append(pattern.getInterval());
                }
                break;
                
            case WEEKLY:
                rrule.append("FREQ=WEEKLY");
                if (pattern.getInterval() != null && pattern.getInterval() > 1) {
                    rrule.append(";INTERVAL=").append(pattern.getInterval());
                }
                if (pattern.getDaysOfWeek() != null && !pattern.getDaysOfWeek().isEmpty()) {
                    rrule.append(";BYDAY=");
                    rrule.append(pattern.getDaysOfWeek().stream()
                            .map(RRuleUtils::dayOfWeekToByDay)
                            .collect(Collectors.joining(",")));
                }
                break;
                
            case MONTHLY:
                rrule.append("FREQ=MONTHLY");
                if (pattern.getInterval() != null && pattern.getInterval() > 1) {
                    rrule.append(";INTERVAL=").append(pattern.getInterval());
                }
                if (pattern.getDayOfMonth() != null) {
                    rrule.append(";BYMONTHDAY=").append(pattern.getDayOfMonth());
                }
                break;
                
            case YEARLY:
                rrule.append("FREQ=YEARLY");
                if (pattern.getInterval() != null && pattern.getInterval() > 1) {
                    rrule.append(";INTERVAL=").append(pattern.getInterval());
                }
                if (pattern.getMonths() != null && !pattern.getMonths().isEmpty()) {
                    rrule.append(";BYMONTH=");
                    rrule.append(pattern.getMonths().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")));
                }
                if (pattern.getDayOfMonth() != null) {
                    rrule.append(";BYMONTHDAY=").append(pattern.getDayOfMonth());
                }
                break;
                
            case CUSTOM:
                // 自定义规则，需要更多参数
                // 根据具体参数判断频率类型
                if (pattern.getDaysOfWeek() != null && !pattern.getDaysOfWeek().isEmpty()) {
                    rrule.append("FREQ=WEEKLY");
                } else if (pattern.getMonths() != null && !pattern.getMonths().isEmpty()) {
                    rrule.append("FREQ=YEARLY");
                } else if (pattern.getDayOfMonth() != null) {
                    rrule.append("FREQ=MONTHLY");
                } else {
                    rrule.append("FREQ=DAILY");
                }
                
                if (pattern.getInterval() != null && pattern.getInterval() > 1) {
                    rrule.append(";INTERVAL=").append(pattern.getInterval());
                }
                
                // 添加自定义参数
                if (pattern.getDaysOfWeek() != null && !pattern.getDaysOfWeek().isEmpty()) {
                    rrule.append(";BYDAY=");
                    rrule.append(pattern.getDaysOfWeek().stream()
                            .map(RRuleUtils::dayOfWeekToByDay)
                            .collect(Collectors.joining(",")));
                }
                
                if (pattern.getMonths() != null && !pattern.getMonths().isEmpty()) {
                    rrule.append(";BYMONTH=");
                    rrule.append(pattern.getMonths().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")));
                }
                
                if (pattern.getDayOfMonth() != null) {
                    rrule.append(";BYMONTHDAY=").append(pattern.getDayOfMonth());
                }
                break;
        }
        
        return rrule.toString();
    }
    
    /**
     * 将iCal RRULE字符串解析为RecurrencePattern对象
     * @param rruleStr iCal RRULE字符串
     * @return RecurrencePattern对象
     */
    public static RecurrencePattern fromRRule(String rruleStr) {
        if (rruleStr == null || rruleStr.isEmpty()) {
            return null;
        }
        
        RecurrencePattern pattern = new RecurrencePattern();
        
        // 移除可能的前缀
        if (rruleStr.startsWith("RRULE:")) {
            rruleStr = rruleStr.substring(6);
        }
        
        String[] parts = rruleStr.split(";");
        String freq = null;
        
        for (String part : parts) {
            String[] keyValue = part.split("=", 2);
            if (keyValue.length != 2) {
                continue;
            }
            
            String key = keyValue[0].toUpperCase();
            String value = keyValue[1];
            
            switch (key) {
                case "FREQ":
                    freq = value.toUpperCase();
                    switch (freq) {
                        case "DAILY":
                            pattern.setFrequency(RecurrencePattern.Frequency.DAILY);
                            break;
                        case "WEEKLY":
                            pattern.setFrequency(RecurrencePattern.Frequency.WEEKLY);
                            break;
                        case "MONTHLY":
                            pattern.setFrequency(RecurrencePattern.Frequency.MONTHLY);
                            break;
                        case "YEARLY":
                            pattern.setFrequency(RecurrencePattern.Frequency.YEARLY);
                            break;
                    }
                    break;
                    
                case "INTERVAL":
                    try {
                        pattern.setInterval(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        // 忽略无效的数字
                    }
                    break;
                    
                case "BYDAY":
                    String[] days = value.split(",");
                    List<DayOfWeek> dayOfWeekList = new ArrayList<>();
                    for (String day : days) {
                        DayOfWeek dayOfWeek = byDayToDayOfWeek(day);
                        if (dayOfWeek != null) {
                            dayOfWeekList.add(dayOfWeek);
                        }
                    }
                    pattern.setDaysOfWeek(dayOfWeekList);
                    break;
                    
                case "BYMONTH":
                    String[] months = value.split(",");
                    List<Integer> monthList = new ArrayList<>();
                    for (String month : months) {
                        try {
                            monthList.add(Integer.parseInt(month));
                        } catch (NumberFormatException e) {
                            // 忽略无效的数字
                        }
                    }
                    pattern.setMonths(monthList);
                    break;
                    
                case "BYMONTHDAY":
                    try {
                        pattern.setDayOfMonth(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        // 忽略无效的数字
                    }
                    break;
            }
        }
        
        // 如果有复杂的参数组合，则设置为自定义类型
        if ((pattern.getDaysOfWeek() != null && !pattern.getDaysOfWeek().isEmpty()) ||
            (pattern.getMonths() != null && !pattern.getMonths().isEmpty()) ||
            (pattern.getDayOfMonth() != null)) {
            
            // 只有当参数组合不匹配基本类型时才设置为CUSTOM
            if (!isBasicPattern(pattern, freq)) {
                pattern.setFrequency(RecurrencePattern.Frequency.CUSTOM);
            }
        }
        
        return pattern;
    }
    
    /**
     * 判断是否为基本模式
     * @param pattern 重复模式
     * @param freq 频率
     * @return 是否为基本模式
     */
    private static boolean isBasicPattern(RecurrencePattern pattern, String freq) {
        if (freq == null) return false;
        
        switch (freq) {
            case "WEEKLY":
                // 基本的每周模式：没有指定星期几或者只指定一个与起始日期匹配的星期几
                return pattern.getDaysOfWeek() == null || pattern.getDaysOfWeek().isEmpty() ||
                       (pattern.getDaysOfWeek().size() == 1);
            case "MONTHLY":
                // 基本的每月模式：没有指定具体日期或者只指定一个日期
                return pattern.getDayOfMonth() == null ||
                       (pattern.getDaysOfWeek() == null || pattern.getDaysOfWeek().isEmpty());
            case "YEARLY":
                // 基本的每年模式：没有指定月份或者只指定一个月份，没有指定星期几
                return (pattern.getMonths() == null || pattern.getMonths().size() <= 1) &&
                       (pattern.getDaysOfWeek() == null || pattern.getDaysOfWeek().isEmpty());
            default:
                return pattern.getDaysOfWeek() == null && 
                       pattern.getMonths() == null && 
                       pattern.getDayOfMonth() == null;
        }
    }
    
    /**
     * 将DayOfWeek转换为iCal BYDAY格式
     * @param dayOfWeek DayOfWeek枚举
     * @return iCal BYDAY字符串
     */
    private static String dayOfWeekToByDay(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return "MO";
            case TUESDAY: return "TU";
            case WEDNESDAY: return "WE";
            case THURSDAY: return "TH";
            case FRIDAY: return "FR";
            case SATURDAY: return "SA";
            case SUNDAY: return "SU";
            default: return "";
        }
    }
    
    /**
     * 将iCal BYDAY格式转换为DayOfWeek
     * @param byDay iCal BYDAY字符串
     * @return DayOfWeek枚举
     */
    private static DayOfWeek byDayToDayOfWeek(String byDay) {
        // 处理带数字前缀的情况，如"2MO"表示第二个周一
        if (byDay.length() > 2) {
            byDay = byDay.substring(byDay.length() - 2); // 只取最后两个字符
        }
        
        switch (byDay.toUpperCase()) {
            case "MO": return DayOfWeek.MONDAY;
            case "TU": return DayOfWeek.TUESDAY;
            case "WE": return DayOfWeek.WEDNESDAY;
            case "TH": return DayOfWeek.THURSDAY;
            case "FR": return DayOfWeek.FRIDAY;
            case "SA": return DayOfWeek.SATURDAY;
            case "SU": return DayOfWeek.SUNDAY;
            default: return null;
        }
    }
}