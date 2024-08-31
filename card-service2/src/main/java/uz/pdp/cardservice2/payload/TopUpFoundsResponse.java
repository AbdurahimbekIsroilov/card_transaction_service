package uz.pdp.cardservice2.payload;


import lombok.*;
import uz.pdp.cardservice2.enums.CurrencyEnum;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopUpFoundsResponse {

    private UUID transactionId;
    private UUID externalId;
    private UUID cardId;
    private BigDecimal afterBalance;
    private BigDecimal amount;
    private CurrencyEnum currency;
    private Long exchangeRate;

}
