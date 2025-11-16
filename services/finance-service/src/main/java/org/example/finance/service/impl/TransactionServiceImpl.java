package org.example.finance.service.impl;

import org.example.finance.entity.Transaction;
import org.example.finance.dto.TransactionCreateRequest;
import org.example.finance.mapper.TransactionMapper;
import org.example.finance.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

        transactionMapper.insert(transaction);

        // 简化版本：只记录日志，不发送消息队列
        System.out.println("新交易创建 - 用户: " + userId +
                ", 金额: " + request.getAmount() +
                ", 类型: " + request.getType());

        return transactionMapper.findById(transaction.getId());
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
        // 简化版本：空实现，需要时再集成消息队列
        System.out.println("交易事件发布（模拟）: " + transaction.getId());
    }
}