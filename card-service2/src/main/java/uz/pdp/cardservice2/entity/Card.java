package uz.pdp.cardservice2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import uz.pdp.cardservice2.enums.CardStatusEnum;
import uz.pdp.cardservice2.enums.CurrencyEnum;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "cards")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;


    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false,name = "card_status")
    @Enumerated(EnumType.STRING)
    private CardStatusEnum cardStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency = CurrencyEnum.UZS;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private UUID idempotencyKey;

    @Column(name = "e_tag",nullable = false,unique = true)
    private String etag;

}
