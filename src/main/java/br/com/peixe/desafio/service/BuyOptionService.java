package br.com.peixe.desafio.service;

import br.com.peixe.desafio.model.dto.BuyOptionDTO;
import br.com.peixe.desafio.model.entity.BuyOption;
import br.com.peixe.desafio.repository.BuyOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class BuyOptionService {

    private static final String MSG_NOT_FOUND = "Não foi possível localizar a opção de compra informada!";
    private static final String NO_QUANTITY_TO_SELL = "Todas as unidades dessa oferta já foram vendidas =( Sorry!";

    private final BuyOptionRepository buyOptionRepository;
    private final DealService dealService;

    @Autowired public BuyOptionService(BuyOptionRepository buyOptionRepository, DealService dealService) {
        this.buyOptionRepository = buyOptionRepository;
        this.dealService = dealService;
    }

    @Transactional
    public BuyOptionDTO insert(BuyOptionDTO buyOptionDTO) {

        Double salePrice = buyOptionDTO.getSalePriceCalculated();
        buyOptionDTO.setSalePrice(salePrice);

        BuyOption saved = buyOptionRepository.save(new BuyOption(buyOptionDTO));

        return new BuyOptionDTO(saved);
    }

    @Transactional(readOnly = true)
    public BuyOption findById(Long id) {
        return buyOptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MSG_NOT_FOUND));
    }

    @Transactional
    public void sellUnit(Long id) {

        BuyOption buyOption = buyOptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MSG_NOT_FOUND));

        if (buyOption.getQuantityCupom() <= 0) {
            throw new RuntimeException(NO_QUANTITY_TO_SELL);
        }

        buyOption.setQuantityCupom(buyOption.getQuantityCupom()-1);
        BuyOption saved = buyOptionRepository.save(buyOption);

        dealService.updateTotalSold(new BuyOptionDTO(saved));
    }

    public List<BuyOptionDTO> findAllWithPublishDateValid() {
        return buyOptionRepository.findAllWithPublishDateValid(LocalDate.now())
                .stream().map(BuyOptionDTO::new).collect(toList());
    }
}
