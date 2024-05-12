package com.example.linkplustask.repository;

import com.example.linkplustask.model.Account;
import com.example.linkplustask.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> getAllBySourceAccount(Account account);
    List<Transaction> getAllByDestinationAccount(Account account);
}
