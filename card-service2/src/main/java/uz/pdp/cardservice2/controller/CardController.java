package uz.pdp.cardservice2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cardservice2.payload.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
public interface CardController {

    @PostMapping
    ResponseEntity<CardResponseDTO> createCard(
            @RequestHeader(value = "Idempotency-Key", required = true) UUID idempotencyKey,
            @RequestBody CardRequestDTO cardRequestDTO);

    @GetMapping("/{cardId}")
    ResponseEntity<CardResponseDTO> getCard(
            @PathVariable UUID cardId,
            @RequestHeader("User-Id") Long userId);

    @PostMapping("/{cardId}/block")
    ResponseEntity<ApiResult<Void>> blockCard(
            @PathVariable UUID cardId,
            @RequestHeader("If-Match") String eTag);

    @PostMapping("/{cardId}/unblock")
    ResponseEntity<ApiResult<Void>> unblockCard(
            @PathVariable UUID cardId,
            @RequestHeader("If-Match") String ifMatch);

    @PostMapping("/{cardId}/debit")
    ResponseEntity<WithdrawFundsResponse> withdrawFunds(
            @PathVariable UUID cardId,
            @RequestHeader("Idempotency-Key") UUID idempotencyKey,
            @RequestBody WithdrawFundsRequest requestDTO);

    @PostMapping("/{cardId}/credit")
    ResponseEntity<TopUpFoundsResponse> topUpFunds(
            @PathVariable UUID cardId,
            @RequestHeader("Idempotency-Key") UUID idempotencyKey,
            @RequestBody TopUpFoundsRequest requestDTO);
}
