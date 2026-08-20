package com.example.withdrawal;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Entity
@Table(name = "withdrawals")
public class Withdrawal {

    @Id
    private String transactionId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status;

    private String qrToken;

    private OffsetDateTime createdAt;

    private OffsetDateTime expiredAt;

    public enum WithdrawalStatus {
        PENDING, SUCCESS, EXPIRED, CANCELLED
    }

    public Withdrawal() {}

    public Withdrawal(String transactionId, BigDecimal amount, String qrToken, OffsetDateTime expiredAt) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.status = WithdrawalStatus.PENDING;
        this.qrToken = qrToken;
        this.createdAt = OffsetDateTime.now();
        this.expiredAt = expiredAt;
    }

    public String getTransactionId() { return transactionId; }
    public BigDecimal getAmount() { return amount; }
    public WithdrawalStatus getStatus() { return status; }
    public void setStatus(WithdrawalStatus status) { this.status = status; }
    public String getQrToken() { return qrToken; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getExpiredAt() { return expiredAt; }
}
