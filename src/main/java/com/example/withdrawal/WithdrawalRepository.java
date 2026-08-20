package com.example.withdrawal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, String> {

    Optional<Withdrawal> findByTransactionId(String transactionId);

    @Modifying
    @Query("UPDATE Withdrawal w SET w.status = :newStatus " +
           "WHERE w.transactionId = :id AND w.status = 'PENDING' AND w.expiredAt > :now")
    int updateStatusAtomic(@Param("id") String transactionId, 
                           @Param("newStatus") Withdrawal.WithdrawalStatus newStatus, 
                           @Param("now") OffsetDateTime now);
}
