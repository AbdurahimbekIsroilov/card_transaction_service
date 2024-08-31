package uz.pdp.cardservice2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cardservice2.entity.Card;
import uz.pdp.cardservice2.enums.CardStatusEnum;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    Optional<Card> findByIdempotencyKey(UUID idempotencyKey);


    long countByUserIdAndCardStatusNot(Long userId, CardStatusEnum cardStatusEnum);
}
