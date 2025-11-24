package org.example.finance.controller;

import org.example.finance.dto.MonthlyStatsDTO;
import org.example.finance.dto.TransactionCreateRequest;
import org.example.finance.dto.TransactionDTO;
import org.example.finance.entity.Transaction;
import org.example.finance.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getUserTransactions(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestParam(value = "X-User-Id", required = false) Long userIdParam) {
        try {
            Long actualUserId = getActualUserId(userId, userIdParam);
            if (actualUserId == null) {
                return ResponseEntity.badRequest().build();
            }

            List<Transaction> transactions = transactionService.getUserTransactions(actualUserId);
            List<TransactionDTO> transactionDTOs = transactions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(transactionDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<TransactionDTO>> getRecentTransactions(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestParam(value = "X-User-Id", required = false) Long userIdParam) {
        try {
            Long actualUserId = getActualUserId(userId, userIdParam);
            if (actualUserId == null) {
                return ResponseEntity.badRequest().build();
            }

            List<Transaction> transactions = transactionService.getRecentTransactions(actualUserId, 20);

            List<TransactionDTO> recentTransactions = transactions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(recentTransactions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/stats/monthly")
    public ResponseEntity<MonthlyStatsDTO> getMonthlyStats(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestParam(value = "X-User-Id", required = false) Long userIdParam,
            @RequestParam int year,
            @RequestParam int month) {
        try {
            Long actualUserId = getActualUserId(userId, userIdParam);
            if (actualUserId == null) {
                return ResponseEntity.badRequest().build();
            }

            BigDecimal totalIncome = transactionService.getMonthlyIncome(actualUserId, year, month);
            BigDecimal totalExpense = transactionService.getMonthlyExpense(actualUserId, year, month);

            MonthlyStatsDTO stats = new MonthlyStatsDTO();
            stats.setTotalIncome(totalIncome);
            stats.setTotalExpense(totalExpense);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestParam(value = "X-User-Id", required = false) Long userIdParam,
            @RequestBody TransactionCreateRequest request) {
        try {
            Long actualUserId = getActualUserId(userId, userIdParam);
            if (actualUserId == null) {
                return ResponseEntity.badRequest().build();
            }

            Transaction transaction = transactionService.createTransaction(actualUserId, request);
            TransactionDTO transactionDTO = convertToDTO(transaction);
            return ResponseEntity.ok(transactionDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @PathVariable Long id) {
        try {
            if (userId == null) {
                return ResponseEntity.badRequest().build();
            }

            // 验证交易是否属于该用户
            Transaction transaction = transactionService.getTransactionById(id);
            if (transaction == null || !transaction.getUserId().equals(userId)) {
                return ResponseEntity.notFound().build();
            }

            transactionService.deleteTransaction(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    private Long getActualUserId(Long headerUserId, Long paramUserId) {
        return headerUserId != null ? headerUserId : paramUserId;
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setDescription(transaction.getDescription());
        dto.setCategoryId(transaction.getCategoryId());
        dto.setCurrencyCode(transaction.getCurrencyCode());
        dto.setCategoryName(transaction.getCategoryName());

        return dto;
    }
}