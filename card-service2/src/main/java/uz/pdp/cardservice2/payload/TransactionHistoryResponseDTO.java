package uz.pdp.cardservice2.payload;

import lombok.*;
import uz.pdp.cardservice2.enums.CurrencyEnum;
import uz.pdp.cardservice2.enums.Purpose;
import uz.pdp.cardservice2.enums.TransactionType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionHistoryResponseDTO {

    private int page;
    private int size;
    private int totalPages;
    private long totalItems;
    private List<TransactionDTO> content;
}
