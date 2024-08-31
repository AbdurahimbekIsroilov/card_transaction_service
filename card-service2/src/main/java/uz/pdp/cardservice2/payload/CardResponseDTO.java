package uz.pdp.cardservice2.payload;

import lombok.*;
import uz.pdp.cardservice2.enums.CardStatusEnum;
import uz.pdp.cardservice2.enums.CurrencyEnum;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDTO {
    private UUID cardId;
    private Long userId;
    private CardStatusEnum status;
    private BigDecimal balance;
    private CurrencyEnum currency;
}
