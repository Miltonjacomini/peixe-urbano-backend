package br.com.peixe.desafio.service;

import br.com.peixe.desafio.model.dto.BuyOptionDTO;
import br.com.peixe.desafio.model.dto.DealDTO;
import br.com.peixe.desafio.model.entity.BuyOption;
import br.com.peixe.desafio.model.entity.Deal;
import br.com.peixe.desafio.repository.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DealService {

    private static final String OVER_LIMIT_PUBLISH = "Oferta não respeita as regras de publicação";
    private static final String DEAL_NOT_FOUND = "Não foi possível localizar a oferta infomada!";

    private final DealRepository dealRepository;

    @Autowired public DealService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Transactional
    public DealDTO insert(DealDTO dealDTO) {

        Deal deal = new Deal(dealDTO);

        if (deal.getPublishDate().isBefore(LocalDate.now()) ||
            deal.getEndDate().isBefore(LocalDate.now()) ||
            deal.getEndDate().isBefore(deal.getPublishDate())) {
            throw new RuntimeException(OVER_LIMIT_PUBLISH);
        }

        Deal saved = dealRepository.save(deal);

        return new DealDTO(saved);
    }

    @Transactional
    public DealDTO addOption(Long id, BuyOptionDTO buyOptionDTO) {

        Deal deal = dealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(DEAL_NOT_FOUND));

        deal.getBuyOptions().add(new BuyOption(buyOptionDTO));

        return new DealDTO(dealRepository.save(deal));
    }

    @Transactional
    public void updateTotalSold(BuyOptionDTO buyOptionDTO) {

        Deal deal = findById(buyOptionDTO.getDealId());
        deal.setTotalSold(deal.getTotalSold()+1);

        dealRepository.save(deal);
    }

    @Transactional(readOnly = true)
    public Deal findById(Long id) {
        return dealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(DEAL_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<DealDTO> findAllWithPublishDateValid() {
        return dealRepository.findAllWithPublishDateValid(LocalDate.now())
                .stream().map(DealDTO::new).collect(toList());
    }
}
