package uz.pdp.cardservice2.payload;

import lombok.*;
import org.springframework.beans.propertyeditors.CurrencyEditor;
import uz.pdp.cardservice2.enums.CurrencyEnum;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopUpFoundsRequest {

    private UUID externalId;
    private long amount;
    private CurrencyEnum currency;

}
