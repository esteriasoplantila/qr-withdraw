package com.example.withdrawal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/withdrawals")
public class WithdrawalController {

    private final WithdrawalService service;

    public WithdrawalController(WithdrawalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<WithdrawalResponse> create(@RequestBody CreateWithdrawalRequest request) {
        Withdrawal withdrawal = service.createWithdrawal(request.getAmount());
        return ResponseEntity.ok(new WithdrawalResponse(withdrawal, "Transaksi berhasil dibuat"));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<WithdrawalResponse> getStatus(@PathVariable String transactionId) {
        try {
            Withdrawal withdrawal = service.getWithdrawal(transactionId);
            return ResponseEntity.ok(new WithdrawalResponse(withdrawal, "Status transaksi berhasil diambil"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{transactionId}/redeem")
    public ResponseEntity<WithdrawalResponse> redeem(@PathVariable String transactionId) {
        try {
            Withdrawal withdrawal = service.redeemWithdrawal(transactionId);
            return ResponseEntity.ok(new WithdrawalResponse(withdrawal, "Redeem berhasil"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new WithdrawalResponse(
                    service.getWithdrawal(transactionId), 
                    e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
