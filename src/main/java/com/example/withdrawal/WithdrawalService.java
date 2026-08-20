package com.example.withdrawal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class WithdrawalService {

    private final WithdrawalRepository repository;

    @Value("${poc.withdrawal.qr-expiry-seconds:180}")
    private long qrExpirySeconds;

    public WithdrawalService(WithdrawalRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Withdrawal createWithdrawal(BigDecimal amount) {
        String transactionId = "WD-" + String.format("%06d", System.currentTimeMillis() % 1000000);
        String qrToken = UUID.randomUUID().toString();
        OffsetDateTime expiredAt = OffsetDateTime.now().plusSeconds(qrExpirySeconds);

        Withdrawal withdrawal = new Withdrawal(transactionId, amount, qrToken, expiredAt);
        return repository.save(withdrawal);
    }

    public Withdrawal getWithdrawal(String transactionId) {
        Withdrawal withdrawal = repository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan"));

        if (withdrawal.getStatus() == Withdrawal.WithdrawalStatus.PENDING 
                && OffsetDateTime.now().isAfter(withdrawal.getExpiredAt())) {
            withdrawal.setStatus(Withdrawal.WithdrawalStatus.EXPIRED);
            repository.save(withdrawal);
        }

        return withdrawal;
    }

    @Transactional
    public Withdrawal redeemWithdrawal(String transactionId) {
        Withdrawal withdrawal = repository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan"));

        if (withdrawal.getStatus() == Withdrawal.WithdrawalStatus.SUCCESS) {
            return withdrawal;
        }

        if (OffsetDateTime.now().isAfter(withdrawal.getExpiredAt())) {
            withdrawal.setStatus(Withdrawal.WithdrawalStatus.EXPIRED);
            repository.save(withdrawal);
            throw new IllegalStateException("QR Code telah kadaluwarsa");
        }

        int updatedRows = repository.updateStatusAtomic(
                transactionId, 
                Withdrawal.WithdrawalStatus.SUCCESS, 
                OffsetDateTime.now()
        );

        if (updatedRows == 0) {
            throw new IllegalStateException("Gagal melakukan redeem. Transaksi sudah diproses atau expired.");
        }

        withdrawal.setStatus(Withdrawal.WithdrawalStatus.SUCCESS);
        return withdrawal;
    }
}
