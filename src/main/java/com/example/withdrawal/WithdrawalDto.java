package com.example.withdrawal;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

class CreateWithdrawalRequest {
    private BigDecimal amount;

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}

class WithdrawalResponse {
    private String transactionId;
    private BigDecimal amount;
    private String status;
    private String qrToken;
    private OffsetDateTime expiredAt;
    private String message;

    public WithdrawalResponse(Withdrawal entity, String message) {
        this.transactionId = entity.getTransactionId();
        this.amount = entity.getAmount();
        this.status = entity.getStatus().name();
        this.qrToken = entity.getQrToken();
        this.expiredAt = entity.getExpiredAt();
        this.message = message;
    }

    public String getTransactionId() { return transactionId; }
    public BigDecimal getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getQrToken() { return qrToken; }
    public OffsetDateTime getExpiredAt() { return expiredAt; }
    public String getMessage() { return message; }
}
