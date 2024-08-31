package uz.pdp.cardservice2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cardservice2.entity.Transaction;
import uz.pdp.cardservice2.enums.TransactionType;

import java.awt.print.Pageable;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Page<Transaction> findByCardId(UUID cardId, Pageable pageable);
}
