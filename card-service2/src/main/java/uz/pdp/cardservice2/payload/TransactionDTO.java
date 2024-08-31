package uz.pdp.cardservice2.payload;

import lombok.*;
import uz.pdp.cardservice2.enums.CurrencyEnum;
import uz.pdp.cardservice2.enums.Purpose;
import uz.pdp.cardservice2.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {

    private UUID transaction_id;
    private UUID external_id;
    private UUID card_id;
    private BigDecimal after_balance;
    private BigDecimal amount;
    private CurrencyEnum currency;
    private TransactionType type;
    private Purpose purpose;
    private BigDecimal exchange_rate;
}
