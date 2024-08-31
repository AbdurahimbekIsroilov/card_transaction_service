package uz.pdp.cardservice2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cardservice2.enums.TransactionType;
import uz.pdp.cardservice2.payload.TransactionHistoryResponseDTO;

import java.util.Currency;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
public interface TransactionController {

    @GetMapping("/{cardId}/transactions")
    ResponseEntity<TransactionHistoryResponseDTO> getTransactionHistory(
            @PathVariable UUID cardId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size);

}
