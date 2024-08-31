package uz.pdp.cardservice2.payload;

import lombok.*;
import uz.pdp.cardservice2.enums.CurrencyEnum;
import uz.pdp.cardservice2.enums.Purpose;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WithdrawFundsRequest {

    private UUID transactionId;
    private UUID externalId;
    private UUID cardId;
    private BigDecimal afterBalance;
    private BigDecimal amount;
    private CurrencyEnum currency;
    private Purpose purpose;
    private Long exchangeRate;

}
