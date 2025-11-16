package org.example.finance.service;

import org.example.finance.entity.Transaction;
import org.example.finance.dto.TransactionCreateRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    List<Transaction> getUserTransactions(Long userId);
    List<Transaction> getTransactionsByDateRange(Long userId, LocalDateTime start, LocalDateTime end);
    Transaction getTransactionById(Long id);
    Transaction createTransaction(Long userId, TransactionCreateRequest request);
    Transaction updateTransaction(Transaction transaction);
    void deleteTransaction(Long id);
    void publishTransactionEvent(Transaction transaction); // 保留接口，但简化实现
}