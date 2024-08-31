package uz.pdp.cardservice2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uz.pdp.cardservice2.entity.Card;
import uz.pdp.cardservice2.entity.Transaction;
import uz.pdp.cardservice2.entity.User;
import uz.pdp.cardservice2.enums.CardStatusEnum;
import uz.pdp.cardservice2.enums.CurrencyEnum;
import uz.pdp.cardservice2.exceptions.RestException;
import uz.pdp.cardservice2.mapper.TransactionMapper;
import uz.pdp.cardservice2.payload.*;
import uz.pdp.cardservice2.repository.CardRepository;
import uz.pdp.cardservice2.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.cardservice2.utils.MessageConstants;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    @Autowired
    private final CardRepository cardRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final TransactionService transactionService;

    @Autowired
    private final TransactionMapper transactionMapper;

    @Autowired
    private final CurrencyService currencyService;

    @Override
    public CardResponseDTO createCard(CardRequestDTO cardRequestDTO, UUID idempotencyKey) {
        Optional<Card> existingCard = cardRepository.findByIdempotencyKey(idempotencyKey);

        if (existingCard.isPresent()) {

            return convertToCardResponseDTO(existingCard.get());
        }

        User user = userRepository.findById(cardRequestDTO.getUserId())
                .orElseThrow(() -> new RestException(MessageConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        long cardCount = cardRepository.countByUserIdAndCardStatusNot(cardRequestDTO.getUserId(), CardStatusEnum.CLOSED);
        if (cardCount >= 3) {
            throw new RestException(MessageConstants.NUMBER_CARDS_EXCEEDED_LIMIT,HttpStatus.BAD_REQUEST);
        }

        Card card = Card.builder()
                .cardNumber(UUID.randomUUID().toString())
                .balance(cardRequestDTO.getInitialAmount())
                .cardStatus(cardRequestDTO.getStatus())
                .user(user)
                .idempotencyKey(idempotencyKey)
                .currency(cardRequestDTO.getCurrency())
                .build();
        card.setEtag(String.valueOf(card.hashCode()));

        cardRepository.save(card);

        return convertToCardResponseDTO(card);
    }




    @Transactional(readOnly = true)
    public CardResponseDTO getCardById(UUID cardId, Long userId) {
        Optional<Card> optionalCard = cardRepository.findById(cardId);
        if (optionalCard.isEmpty()) {
            throw new RestException(MessageConstants.CARD_NOT_EXISTS_PROGRESSING,HttpStatus.NOT_FOUND);
        }

        Card card = optionalCard.get();

        if (!card.getUser().getId().equals(userId)) {
            throw new RestException(MessageConstants.USER_NOT_AUTHORIZED,HttpStatus.FORBIDDEN);
        }

        return CardResponseDTO.builder()
                .cardId(card.getId())
                .userId(card.getUser().getId())
                .status(card.getCardStatus())
                .balance(card.getBalance())
                .currency(CurrencyEnum.UZS)
                .build();
    }


    @Override
    public void blockCard(UUID cardId, String ifMatch) {

        Card card = cardRepository.findById(cardId).orElseThrow(() ->
                RestException.restThrow(MessageConstants.CARD_NOT_EXISTS_PROGRESSING, HttpStatus.NOT_FOUND));


        if (!card.getEtag().equals(ifMatch)) {
            throw RestException.restThrow(MessageConstants.CARD_NOT_EXISTS_PROGRESSING, HttpStatus.NOT_FOUND);
        }


        if (!card.getCardStatus().equals(CardStatusEnum.ACTIVE)) {
            throw RestException.restThrow(MessageConstants.ACTIVE_CARDS_BLOCKED, HttpStatus.BAD_REQUEST);
        }


        card.setCardStatus(CardStatusEnum.BLOCK);
        card.setEtag(generateEtag(card));
        cardRepository.save(card);
    }

    @Override
    public void unblockCard(UUID cardId, String ifMatch) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RestException(MessageConstants.CARD_NOT_EXISTS_PROGRESSING, HttpStatus.NOT_FOUND));

        if (!card.getEtag().equals(ifMatch)) {
            throw new RestException(MessageConstants.CARD_NOT_EXISTS_PROGRESSING, HttpStatus.NOT_FOUND);
        }

        if (!card.getCardStatus().equals(CardStatusEnum.BLOCK)) {
            throw new RestException(MessageConstants.BLOCK_CARDS_UNBLOCKED, HttpStatus.BAD_REQUEST);
        }

        card.setCardStatus(CardStatusEnum.ACTIVE);
        card.setEtag(generateEtag(card));
        cardRepository.save(card);
    }




    @Override
    @Transactional
    public WithdrawFundsResponse withdrawFunds(UUID cardId, UUID idempotencyKey, WithdrawFundsRequest requestDTO) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RestException(MessageConstants.CARD_NOT_FOUND,HttpStatus.NOT_FOUND));
        if (!card.getCardStatus().equals(CardStatusEnum.ACTIVE)) {
            throw new RestException(MessageConstants.IS_NOT_ACTIVE, HttpStatus.BAD_REQUEST);
        }

        BigDecimal amount = requestDTO.getAmount();
        if (card.getBalance().compareTo(amount) < 0) {
            throw new RestException(MessageConstants.INSUFFICIENT_FUNDS,HttpStatus.FORBIDDEN);
        }

        BigDecimal exchangeRate = BigDecimal.ZERO;
        if (!requestDTO.getCurrency().equals(card.getCurrency())) {
            exchangeRate = getExchangeRate(card.getCurrency(), requestDTO.getCurrency());
            amount = convertCurrency(amount, exchangeRate);
        }


        card.setBalance(card.getBalance().subtract(amount));
        cardRepository.save(card);


        Transaction transaction = transactionMapper.withdrawFundsRequestToTransaction(requestDTO, amount, exchangeRate);
        transactionService.saveTransaction(transaction);

        return transactionMapper.toResponseDTO(transaction);
    }

    @Override
    @Transactional
    public TopUpFoundsResponse topUpFunds(UUID cardId, UUID idempotencyKey, TopUpFoundsRequest requestDTO) {

        Card card = cardRepository.findById(cardId).
                orElseThrow(() -> new RestException(MessageConstants.CARD_NOT_FOUND,HttpStatus.NOT_FOUND));
        if (!card.getCardStatus().equals(CardStatusEnum.ACTIVE)) {
            throw new RestException(MessageConstants.CARD_IS_NOT_ACTIVE,HttpStatus.FORBIDDEN);
        }

        BigDecimal amount = BigDecimal.valueOf(requestDTO.getAmount());
        BigDecimal exchangeRate = BigDecimal.ZERO;
        if (!requestDTO.getCurrency().equals(card.getCurrency())) {
            exchangeRate = getExchangeRate(card.getCurrency(), requestDTO.getCurrency());
            amount = convertCurrency(amount, exchangeRate);

        }

        card.setBalance(card.getBalance().add(amount));
        cardRepository.save(card);

        Transaction transaction = transactionMapper.topUpFundsToTransaction(requestDTO, amount, exchangeRate,cardId);
        transactionService.saveTransaction(transaction);


        return transactionMapper.transactionToTopUpFundsResponse(transaction);
    }

    private BigDecimal getExchangeRate(CurrencyEnum from, CurrencyEnum to) {


        Mono<Double> rateById = currencyService.getRateById(68);
        Double block = rateById.block();

        BigDecimal exchangeRate=BigDecimal.valueOf(block);
        if(from.equals(CurrencyEnum.UZS)){
            exchangeRate=BigDecimal.valueOf(-1*block);
        }



        return exchangeRate;
    }

    private BigDecimal convertCurrency(BigDecimal amount, BigDecimal exchangeRate) {

        int result = exchangeRate.compareTo(BigDecimal.ZERO);

        BigDecimal money=BigDecimal.ZERO;
        if(result>0){
            money=amount.multiply(exchangeRate);
        }
        else{
            money=amount.divide(exchangeRate);
        }

        return money;
    }



    private String generateEtag(Card card) {
        return Integer.toHexString(card.hashCode());
    }

    private CardResponseDTO convertToCardResponseDTO(Card card) {
        CardResponseDTO responseDTO = new CardResponseDTO();
        responseDTO.setCardId(card.getId());
        responseDTO.setUserId(card.getUser().getId());
        responseDTO.setStatus(card.getCardStatus());
        responseDTO.setBalance(card.getBalance());
        responseDTO.setCurrency(card.getCurrency());
        return responseDTO;
    }
}
