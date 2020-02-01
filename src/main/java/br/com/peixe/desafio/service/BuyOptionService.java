package br.com.peixe.desafio.service;

import br.com.peixe.desafio.model.entity.BuyOption;
import br.com.peixe.desafio.repository.BuyOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public BuyOption insert(BuyOption buyOption) {


        Double salePrice = Double.valueOf(buyOption.getNormalPrice() * 100 / buyOption.getPercentageDiscount());
        buyOption.setSalePrice(salePrice);

        return buyOptionRepository.save(buyOption);
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

        dealService.updateTotalSold(saved);
    }
}
