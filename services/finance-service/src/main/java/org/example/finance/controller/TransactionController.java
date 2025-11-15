package org.example.finance.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/finance/transactions")
public class TransactionController {

    @GetMapping
    public List<Object> getUserTransactions(@RequestHeader("X-User-Id") Long userId) {
        // 临时返回空列表，后续实现业务逻辑
        return new ArrayList<>();
    }

    @PostMapping
    public String createTransaction(@RequestHeader("X-User-Id") Long userId,
                                    @RequestBody Object transactionRequest) {
        // 临时返回成功消息，后续实现业务逻辑
        return "Transaction created successfully";
    }
}