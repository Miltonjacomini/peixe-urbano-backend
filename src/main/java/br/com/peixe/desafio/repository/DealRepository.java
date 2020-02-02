package br.com.peixe.desafio.repository;

import br.com.peixe.desafio.model.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    @Query(" SELECT d FROM Deal d " +
           "    WHERE d.publishDate <= :today OR d.endDate <= :today ")
    List<Deal> findAllWithPublishDateValid(@Param("today") LocalDate today);

}
