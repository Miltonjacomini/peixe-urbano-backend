package br.com.peixe.desafio.repository;

import br.com.peixe.desafio.model.entity.BuyOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BuyOptionRepository extends JpaRepository<BuyOption, Long> {

    @Query(" SELECT b FROM BuyOption b " +
            "    WHERE (b.startDate <= :today OR b.endDate <= :today)" +
            "       AND b.quantityCupom > 0 " +
            "       AND b.dealId IS NULL")
    List<BuyOption> findAllWithPublishDateValid(LocalDate today);

}
