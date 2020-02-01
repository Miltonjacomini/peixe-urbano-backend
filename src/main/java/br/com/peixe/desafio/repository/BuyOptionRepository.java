package br.com.peixe.desafio.repository;

import br.com.peixe.desafio.model.entity.BuyOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyOptionRepository extends JpaRepository<BuyOption, Long> {
}
