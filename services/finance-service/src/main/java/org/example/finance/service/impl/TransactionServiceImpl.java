package org.example.finance.service.impl;

import org.example.finance.entity.Transaction;
import org.example.finance.dto.TransactionCreateRequest;
import org.example.finance.mapper.TransactionMapper;
import org.example.finance.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    @Override
    public List<Transaction> getUserTransactions(Long userId) {
        return transactionMapper.findByUserId(userId);
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        return transactionMapper.findByUserIdAndDateRange(userId, start, end);
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionMapper.findById(id);
    }

    @Override
    @Transactional
    public Transaction createTransaction(Long userId, TransactionCreateRequest request) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setDescription(request.getDescription());
        transaction.setCategoryId(request.getCategoryId());
        transaction.setCurrencyCode(request.getCurrencyCode());
        transaction.setAiSuggestionJson("{}");

        transactionMapper.insert(transaction);

        Transaction createdTransaction = transactionMapper.findById(transaction.getId());

        System.out.println("新交易创建 - 用户: " + userId +
                ", 金额: " + request.getAmount() +
                ", 类型: " + request.getType() +
                ", 日期: " + request.getTransactionDate() +
                ", 分类: " + request.getCategoryId());

        return createdTransaction;
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        transactionMapper.update(transaction);
        return transactionMapper.findById(transaction.getId());
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionMapper.delete(id);
    }

    @Override
    public void publishTransactionEvent(Transaction transaction) {
        System.out.println("交易事件发布（模拟）: " + transaction.getId());
    }

    @Override
    public List<Transaction> getRecentTransactions(Long userId, int limit) {
        return transactionMapper.findRecentByUserId(userId, limit);
    }

    @Override
    public BigDecimal getMonthlyIncome(Long userId, int year, int month) {
        return transactionMapper.getMonthlyIncome(userId, year, month);
    }

    @Override
    public BigDecimal getMonthlyExpense(Long userId, int year, int month) {
        BigDecimal expense = transactionMapper.getMonthlyExpense(userId, year, month);
        System.out.println("查询本月支出 - 用户: " + userId + ", 年月: " + year + "-" + month + ", 支出: " + expense);
        return expense;
    }
}