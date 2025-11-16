package org.example.finance.mapper;

import org.apache.ibatis.annotations.*;
import org.example.finance.entity.Budget;
import java.util.List;

@Mapper
public interface BudgetMapper {

    @Select("SELECT b.*, c.category_name FROM t_budget b " +
            "LEFT JOIN t_finance_category c ON b.category_id = c.id " +
            "WHERE b.user_id = #{userId} AND b.budget_year = #{year} AND b.budget_month = #{month}")
    List<Budget> findByUserIdAndMonth(@Param("userId") Long userId,
                                      @Param("year") Integer year,
                                      @Param("month") Integer month);

    @Select("SELECT * FROM t_budget WHERE id = #{id}")
    Budget findById(Long id);

    @Insert("INSERT INTO t_budget (user_id, category_id, amount, budget_month, budget_year) " +
            "VALUES (#{userId}, #{categoryId}, #{amount}, #{budgetMonth}, #{budgetYear})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Budget budget);

    @Update("UPDATE t_budget SET amount = #{amount} WHERE id = #{id}")
    int update(Budget budget);

    @Delete("DELETE FROM t_budget WHERE id = #{id}")
    int delete(Long id);

    // 添加缺失的方法
    @Select("SELECT * FROM t_budget WHERE user_id = #{userId} AND category_id = #{categoryId} " +
            "AND budget_year = #{year} AND budget_month = #{month}")
    Budget findByUserCategoryAndMonth(@Param("userId") Long userId,
                                      @Param("categoryId") Long categoryId,
                                      @Param("year") Integer year,
                                      @Param("month") Integer month);
}