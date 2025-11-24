package org.example.finance.mapper;

import org.apache.ibatis.annotations.*;
import org.example.finance.entity.FinanceCategory;
import java.util.List;

@Mapper
public interface FinanceCategoryMapper {

    @Select("SELECT * FROM t_finance_category WHERE user_id = #{userId} OR user_id IS NULL ORDER BY id")
    List<FinanceCategory> findByUserId(Long userId);

    @Select("SELECT * FROM t_finance_category WHERE id = #{id}")
    FinanceCategory findById(Long id);

    @Insert("INSERT INTO t_finance_category (parent_id, user_id, category_name) " +
            "VALUES (#{parentId}, #{userId}, #{categoryName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FinanceCategory category);

    @Update("UPDATE t_finance_category SET category_name = #{categoryName}, parent_id = #{parentId} " +
            "WHERE id = #{id}")
    int update(FinanceCategory category);

    @Delete("DELETE FROM t_finance_category WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT * FROM t_finance_category WHERE user_id IS NULL ORDER BY id")
    List<FinanceCategory> findSystemCategories();

    @Select("SELECT * FROM t_finance_category WHERE category_name = #{categoryName} AND (user_id = #{userId} OR user_id IS NULL)")
    FinanceCategory findByNameAndUserId(@Param("categoryName") String categoryName, @Param("userId") Long userId);
}