package uz.pdp.cardservice2.payload;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.validation.constraints.NotNull;
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
public class WithdrawFundsResponse {

    @NotNull(message = "External ID cannot be null")
    private UUID externalId;

    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

    private CurrencyEnum currency = CurrencyEnum.UZS; // default value

    @NotNull(message = "Purpose cannot be null")
    private Purpose purpose;
}
