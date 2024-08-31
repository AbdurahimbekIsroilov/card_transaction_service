package uz.pdp.cardservice2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uz.pdp.cardservice2.mapper.TransactionMapper;
import uz.pdp.cardservice2.entity.Transaction;
import uz.pdp.cardservice2.payload.TransactionDTO;
import uz.pdp.cardservice2.payload.TransactionHistoryResponseDTO;
import uz.pdp.cardservice2.repository.TransactionRepository;

import java.awt.print.Pageable;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionHistoryResponseDTO getTransactionHistory(UUID cardId, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findByCardId(cardId, pageable);

        Page<TransactionDTO> transactionDTOs = transactions.map(transactionMapper::convertToDTO);


        TransactionHistoryResponseDTO response = new TransactionHistoryResponseDTO();
        response.setPage(transactionDTOs.getNumber());
        response.setSize(transactionDTOs.getSize());
        response.setTotalPages(transactionDTOs.getTotalPages());
        response.setTotalItems(transactions.getTotalElements());
        response.setContent(transactionDTOs.getContent());

        return response;
    }


    @Override
    public TransactionDTO saveTransaction(Transaction transaction) {
        Transaction save = transactionRepository.save(transaction);
        return transactionMapper.convertToDTO(save);
    }
}
