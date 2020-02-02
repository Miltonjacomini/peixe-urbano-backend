package br.com.peixe.desafio.controller;

import br.com.peixe.desafio.model.dto.DealDTO;
import br.com.peixe.desafio.service.DealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DealController {

    private static final Logger logger = LoggerFactory.getLogger(DealController.class);

    protected static final String GET_DEAL = "/deals";
    protected static final String POST_DEAL = "/deal";
    protected static final String POST_DEAL_ADD_OPTION = "/deal/{dealId}/option/{buyOptionId}";

    private final DealService dealService;

    @Autowired
    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @GetMapping(GET_DEAL)
    public ResponseEntity<List<DealDTO>> getAll() {
        return ResponseEntity.ok(dealService.findAllWithPublishDateValid());
    }

    @PostMapping(POST_DEAL)
    public ResponseEntity<DealDTO> save(@Valid @RequestBody DealDTO dealDTO) {

        logger.info("Saving the Deal");

        DealDTO saved = dealService.insert(dealDTO);

        return ResponseEntity.ok(saved);
    }

    @PostMapping(POST_DEAL_ADD_OPTION)
    public ResponseEntity addOption(@Valid @PathVariable Long dealId, @PathVariable Long buyOptionId) {

        logger.info("Adding option to Deal");

        dealService.addOption(dealId, buyOptionId);

        return ResponseEntity.ok("Opção adicionada com sucesso!");
    }
}
