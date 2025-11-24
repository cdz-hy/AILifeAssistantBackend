package org.example.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.user.entity.PersonaMetric;

import java.util.List;

@Mapper
public interface PersonaMetricMapper {
    List<PersonaMetric> listByUserId(@Param("userId") Long userId);
    int upsert(@Param("userId") Long userId, @Param("metricName") String metricName, @Param("metricValue") String metricValue, @Param("lastUpdatedBy") String lastUpdatedBy);
    int deleteAllByUserId(@Param("userId") Long userId);
}