package org.example.finance.mapper;

import org.apache.ibatis.annotations.*;
import org.example.finance.entity.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TransactionMapper {

    @Select("SELECT t.*, c.category_name FROM t_transaction t " +
            "LEFT JOIN t_finance_category c ON t.category_id = c.id " +
            "WHERE t.user_id = #{userId} ORDER BY t.transaction_date DESC")
    List<Transaction> findByUserId(Long userId);

    @Select("SELECT t.*, c.category_name FROM t_transaction t " +
            "LEFT JOIN t_finance_category c ON t.category_id = c.id " +
            "WHERE t.user_id = #{userId} AND t.transaction_date BETWEEN #{start} AND #{end} " +
            "ORDER BY t.transaction_date DESC")
    List<Transaction> findByUserIdAndDateRange(@Param("userId") Long userId,
                                               @Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end);

    @Select("SELECT t.*, c.category_name FROM t_transaction t " +
            "LEFT JOIN t_finance_category c ON t.category_id = c.id " +
            "WHERE t.id = #{id}")
    Transaction findById(Long id);

    @Insert("INSERT INTO t_transaction (user_id, amount, type, transaction_date, description, " +
            "category_id, currency_code, ai_suggestion_json) " +
            "VALUES (#{userId}, #{amount}, #{type}, #{transactionDate}, #{description}, " +
            "#{categoryId}, #{currencyCode}, #{aiSuggestionJson})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Transaction transaction);

    @Update("UPDATE t_transaction SET amount = #{amount}, type = #{type}, " +
            "transaction_date = #{transactionDate}, description = #{description}, " +
            "category_id = #{categoryId}, currency_code = #{currencyCode}, " +
            "ai_suggestion_json = #{aiSuggestionJson} WHERE id = #{id}")
    int update(Transaction transaction);

    @Delete("DELETE FROM t_transaction WHERE id = #{id}")
    int delete(Long id);

    // 添加缺失的方法
    @Select("SELECT COALESCE(SUM(amount), 0) FROM t_transaction " +
            "WHERE user_id = #{userId} AND type = 'expense' " +
            "AND category_id = #{categoryId} " +
            "AND YEAR(transaction_date) = #{year} AND MONTH(transaction_date) = #{month}")
    BigDecimal getMonthlyExpenseByCategory(@Param("userId") Long userId,
                                           @Param("categoryId") Long categoryId,
                                           @Param("year") Integer year,
                                           @Param("month") Integer month);
}