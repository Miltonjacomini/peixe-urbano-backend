package br.com.peixe.desafio.service;

import br.com.peixe.desafio.model.entity.BuyOption;
import br.com.peixe.desafio.model.entity.Deal;
import br.com.peixe.desafio.repository.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DealService {

    private static final String OVER_LIMIT_PUBLISH = "Oferta não respeita as regras de publicação";
    private static final String DEAL_NOT_FOUND = "Não foi possível localizar a oferta infomada!";

    private final DealRepository dealRepository;

    @Autowired public DealService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Transactional
    public Deal insert(Deal deal) {

        if (deal.getPublishDate().isBefore(LocalDate.now()) ||
            deal.getEndDate().isBefore(LocalDate.now()) ||
            deal.getEndDate().isBefore(deal.getPublishDate())) {
            throw new RuntimeException(OVER_LIMIT_PUBLISH);
        }

        return dealRepository.save(deal);
    }

    @Transactional
    public Deal addOption(Long id, BuyOption buyOption) {

        Deal deal = dealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(DEAL_NOT_FOUND));

        deal.getBuyOptions().add(buyOption);

        return dealRepository.save(deal);
    }

    @Transactional
    public void updateTotalSold(BuyOption buyOption) {

        Deal deal = findById(buyOption.getDealId());
        deal.setTotalSold(deal.getTotalSold()+1);

        dealRepository.save(deal);
    }

    @Transactional(readOnly = true)
    public Deal findById(Long id) {
        return dealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(DEAL_NOT_FOUND));
    }
}
