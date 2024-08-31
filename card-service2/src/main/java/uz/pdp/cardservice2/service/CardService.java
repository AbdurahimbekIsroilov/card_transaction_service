package uz.pdp.cardservice2.service;

import org.springframework.stereotype.Service;
import uz.pdp.cardservice2.exceptions.RestException;
import uz.pdp.cardservice2.payload.*;

import java.util.UUID;

@Service
public interface CardService {

    CardResponseDTO createCard(CardRequestDTO cardRequestDTO, UUID idempotencyKey) throws RestException;

    CardResponseDTO getCardById(UUID cardId, Long userId) throws RestException;

    void blockCard(UUID cardId, String ifMatch) throws RestException;

    void unblockCard(UUID cardId, String ifMatch) throws RestException;

    WithdrawFundsResponse withdrawFunds(UUID cardId, UUID idempotencyKey, WithdrawFundsRequest requestDTO) throws RestException;

    TopUpFoundsResponse topUpFunds(UUID cardId, UUID idempotencyKey, TopUpFoundsRequest requestDTO) throws RestException;
}
