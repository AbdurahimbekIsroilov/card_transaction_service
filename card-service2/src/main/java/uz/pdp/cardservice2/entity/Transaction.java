package uz.pdp.cardservice2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import uz.pdp.cardservice2.enums.CurrencyEnum;
import uz.pdp.cardservice2.enums.Purpose;
import uz.pdp.cardservice2.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private UUID transactionId;

    @Column(nullable = false)
    private UUID externalId;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(nullable = false)
    private BigDecimal afterBalance;

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "purpose", nullable = false)
    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    private BigDecimal exchangeRate;

    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime createdAt;


}
