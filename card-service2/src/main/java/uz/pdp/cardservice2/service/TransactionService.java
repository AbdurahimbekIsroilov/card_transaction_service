package uz.pdp.cardservice2.service;

import org.springframework.stereotype.Service;
import uz.pdp.cardservice2.entity.Transaction;
import uz.pdp.cardservice2.payload.TransactionDTO;
import uz.pdp.cardservice2.payload.TransactionHistoryResponseDTO;

import java.awt.print.Pageable;
import java.util.UUID;

@Service
public interface TransactionService {

    public TransactionHistoryResponseDTO getTransactionHistory(UUID cardId, Pageable pageable);

    public TransactionDTO saveTransaction(Transaction transaction);
}
