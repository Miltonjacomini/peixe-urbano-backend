package br.com.peixe.desafio.controller;

import br.com.peixe.desafio.model.dto.BuyOptionDTO;
import br.com.peixe.desafio.service.BuyOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class BuyOptionController {

    private static final Logger logger = LoggerFactory.getLogger(BuyOptionController.class);
    protected static final String POST_BUY_OPTION = "/buy-option";
    protected static final String POST_SELL_UNIT = "/buy-option/{buyOptionId}/sell";

    private final BuyOptionService buyOptionService;

    @Autowired
    public BuyOptionController(BuyOptionService buyOptionService) {
        this.buyOptionService = buyOptionService;
    }


    @PostMapping(POST_BUY_OPTION)
    public ResponseEntity<BuyOptionDTO> save(@Valid @RequestBody BuyOptionDTO buyOptionDTO) {

        logger.info("Saving BuyOption");

        BuyOptionDTO saved = buyOptionService.insert(buyOptionDTO);

        return ResponseEntity.ok(saved);
    }

    @PostMapping(POST_SELL_UNIT)
    public ResponseEntity sellUnit(@Valid @PathVariable Long buyOptionId) {

        logger.info("Selling the unit : %d", buyOptionId);

        buyOptionService.sellUnit(buyOptionId);

        return ResponseEntity.ok("Unidade vendida com sucesso!!");
    }

}
