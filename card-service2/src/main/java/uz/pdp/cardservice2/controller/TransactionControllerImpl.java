package uz.pdp.cardservice2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cardservice2.payload.TransactionHistoryResponseDTO;
import uz.pdp.cardservice2.service.TransactionService;
import uz.pdp.cardservice2.service.TransactionServiceImpl;

import java.awt.print.Pageable;
import java.util.UUID;

@RestController
public class TransactionControllerImpl implements TransactionController{

    @Autowired
    private TransactionService transactionService;

    @Override
    @GetMapping("/{cardId}/transactions")
    public ResponseEntity<TransactionHistoryResponseDTO> getTransactionHistory(
            @PathVariable UUID cardId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = (Pageable) PageRequest.of(page, size);
        TransactionHistoryResponseDTO response = transactionService.getTransactionHistory(cardId, pageable);

        return ResponseEntity.ok(response);
    }

}
