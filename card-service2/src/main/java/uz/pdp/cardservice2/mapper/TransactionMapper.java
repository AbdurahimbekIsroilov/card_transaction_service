package uz.pdp.cardservice2.mapper;

import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import uz.pdp.cardservice2.entity.Card;
import uz.pdp.cardservice2.entity.Transaction;
import uz.pdp.cardservice2.enums.TransactionType;
import uz.pdp.cardservice2.exceptions.RestException;
import uz.pdp.cardservice2.payload.*;
import uz.pdp.cardservice2.repository.CardRepository;
import uz.pdp.cardservice2.utils.MessageConstants;

import java.math.BigDecimal;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class TransactionMapper {

   private final CardRepository cardRepository;

   public Transaction   withdrawFundsRequestToTransaction(WithdrawFundsRequest requestDTO, BigDecimal amount,BigDecimal exchangeRate){


      UUID cardId = requestDTO.getCardId();

      Card card = cardRepository.findById(cardId)
              .orElseThrow(() -> new RestException(MessageConstants.CARD_NOT_FOUND, HttpStatus.NOT_FOUND));

      Transaction transaction = new Transaction();
      transaction.setExternalId(requestDTO.getExternalId());
      transaction.setCard(card);
      transaction.setAmount(amount);
      transaction.setAfterBalance(card.getBalance());
      transaction.setCurrency(card.getCurrency());
      transaction.setType(TransactionType.DEBIT);
      transaction.setPurpose(requestDTO.getPurpose());
      transaction.setExchangeRate(exchangeRate);

      return transaction;
   }

   public TransactionDTO convertToDTO(Transaction transaction) {
      TransactionDTO dto = new TransactionDTO();
      dto.setTransaction_id(transaction.getTransactionId());
      dto.setExternal_id(transaction.getExternalId());
      dto.setCard_id(transaction.getCard().getId());
      dto.setAfter_balance(transaction.getAfterBalance());
      dto.setAmount(transaction.getAmount());
      dto.setCurrency(transaction.getCurrency());
      dto.setType(transaction.getType());
      dto.setPurpose(transaction.getPurpose());
      dto.setExchange_rate(transaction.getExchangeRate());
      return dto;
   }

   public WithdrawFundsResponse toResponseDTO(Transaction transaction) {

      WithdrawFundsResponse withdrawFundsResponse = new WithdrawFundsResponse();
      withdrawFundsResponse.setAmount(transaction.getAmount());
      withdrawFundsResponse.setCurrency(transaction.getCurrency());
      withdrawFundsResponse.setExternalId(transaction.getExternalId());
      withdrawFundsResponse.setPurpose(transaction.getPurpose());
   return withdrawFundsResponse;
   }

   public Transaction topUpFundsToTransaction(TopUpFoundsRequest requestDTO,BigDecimal amount,BigDecimal exchangeRate,UUID cardId){

      Card card = cardRepository.findById(cardId)
              .orElseThrow(() -> new RestException(MessageConstants.CARD_NOT_FOUND, HttpStatus.NOT_FOUND));

      Transaction transaction = new Transaction();
      transaction.setExternalId(requestDTO.getExternalId());
      transaction.setCard(card);
      transaction.setAmount(amount);
      transaction.setAfterBalance(card.getBalance());
      transaction.setCurrency(card.getCurrency());
      transaction.setType(TransactionType.CREDIT);
      transaction.setExchangeRate(exchangeRate);

      return  transaction;
   }

   public  TopUpFoundsResponse transactionToTopUpFundsResponse(Transaction transaction) {
      return TopUpFoundsResponse.builder()
              .transactionId(transaction.getTransactionId())
              .externalId(transaction.getExternalId())
              .cardId(transaction.getCard().getId())
              .afterBalance(transaction.getAfterBalance())
              .amount(transaction.getAmount())
              .currency(transaction.getCurrency())
              .exchangeRate(transaction.getExchangeRate() != null ? transaction.getExchangeRate().longValue() : null)
              .build();
   }


}
