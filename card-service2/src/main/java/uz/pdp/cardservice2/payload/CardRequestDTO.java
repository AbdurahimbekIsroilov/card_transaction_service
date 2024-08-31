package uz.pdp.cardservice2.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.cardservice2.enums.CardStatusEnum;
import uz.pdp.cardservice2.enums.CurrencyEnum;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardRequestDTO {
    private Long userId;
    private CardStatusEnum status = CardStatusEnum.ACTIVE;  // Default value
    private BigDecimal initialAmount;     // Default value
    private CurrencyEnum currency;
}
