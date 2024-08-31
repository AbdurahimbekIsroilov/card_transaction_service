package uz.pdp.cardservice2.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cardservice2.payload.*;
import uz.pdp.cardservice2.service.CardService;

import java.util.UUID;

@RestController
public class CardControllerImpl implements CardController {

    @Autowired
    private CardService cardService;

    @Override
    @PostMapping
    public ResponseEntity<CardResponseDTO> createCard(
            @RequestHeader(value = "Idempotency-Key", required = true) UUID idempotencyKey,
            @RequestBody CardRequestDTO cardRequestDTO) {


            CardResponseDTO responseDTO = cardService.createCard(cardRequestDTO, idempotencyKey);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(responseDTO);

    }

    @Override
    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponseDTO> getCard(
            @PathVariable UUID cardId,
            @RequestHeader("User-Id") Long userId
    ) {

        CardResponseDTO cardById = cardService.getCardById(cardId, userId);

        return ResponseEntity.ok()
                .header("ETag", "\"" + cardById.hashCode() + "\"")
                .body(cardById);
    }

    @Override
    @PostMapping("/{cardId}/block")
    public ResponseEntity<ApiResult<Void>> blockCard(
            @PathVariable UUID cardId,
            @RequestHeader(HttpHeaders.IF_MATCH) String eTag) {

        cardService.blockCard(cardId, eTag);
        return ResponseEntity.noContent().build();  // 204 No Content
    }

    @Override
    @PostMapping("/{cardId}/unblock")
    public ResponseEntity<ApiResult<Void>> unblockCard(
            @PathVariable UUID cardId,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        cardService.unblockCard(cardId, ifMatch);

        return ResponseEntity.noContent().build(); // 204 No Content
    }


    @Override
    @PostMapping("/{cardId}/debit")
    public ResponseEntity<WithdrawFundsResponse> withdrawFunds(
            @PathVariable UUID cardId,
            @RequestHeader("Idempotency-Key") UUID idempotencyKey,
            @Valid @RequestBody WithdrawFundsRequest requestDTO) {

        WithdrawFundsResponse responseDTO = cardService.withdrawFunds(cardId, idempotencyKey, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    @PostMapping("/{cardId}/credit")
    public ResponseEntity<TopUpFoundsResponse> topUpFunds(
            @PathVariable UUID cardId,
            @RequestHeader("Idempotency-Key") UUID idempotencyKey,
            @Valid @RequestBody TopUpFoundsRequest requestDTO) {

        TopUpFoundsResponse responseDTO = cardService.topUpFunds(cardId, idempotencyKey, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

}
