package br.com.peixe.desafio.service;

import br.com.peixe.desafio.model.dto.BuyOptionDTO;
import br.com.peixe.desafio.model.dto.DealDTO;
import br.com.peixe.desafio.model.entity.BuyOption;
import br.com.peixe.desafio.model.entity.Deal;
import br.com.peixe.desafio.model.entity.TypeDeal;
import br.com.peixe.desafio.repository.BuyOptionRepository;
import com.github.javafaker.Faker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "/sql/clearDB.sql",
        executionPhase = AFTER_TEST_METHOD,
        config = @SqlConfig(transactionMode = ISOLATED))
public class BuyOptionServiceTest {

    protected final Faker faker = Faker.instance();

    @Autowired
    private BuyOptionService buyOptionService;

    @Autowired
    private BuyOptionRepository buyOptionRepository;

    @Autowired
    private DealService dealService;

    @Test
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
    public void shouldCreateBuyOption() {

        BuyOptionDTO buyOption = BuyOptionDTO.builder()
                .title(faker.lorem().characters(100))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .normalPrice(Double.valueOf("69.95"))
                .percentageDiscount(Double.valueOf("0.25"))
                .quantityCupom(faker.number().randomNumber())
                .build();

        BuyOptionDTO saved = buyOptionService.insert(buyOption);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("52,46", String.format("%.2f", saved.getSalePrice()));
    }

    @Test
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
    public void shouldSaleBuyOption() {

        DealDTO deal = DealDTO.builder()
                .title(faker.lorem().characters(50))
                .createDate(LocalDate.now())
                .publishDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .text(faker.lorem().characters(200))
                .totalSold(Long.valueOf(0))
                .type(TypeDeal.LOC)
                .buyOptions(Collections.emptyList())
                .build();

        BuyOptionDTO buyOption = BuyOptionDTO.builder()
                .title(faker.lorem().characters(100))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .normalPrice(faker.number().randomDouble(2, 1, 10))
                .percentageDiscount(faker.number().randomDouble(0, 1, 100))
                .quantityCupom(faker.number().randomNumber())
                .build();

        DealDTO dealSaved = dealService.insert(deal);
        buyOption.setDealId(dealSaved.getId());

        BuyOptionDTO saved = buyOptionService.insert(buyOption);

        assertNotNull(saved.getId());

        buyOptionService.sellUnit(saved.getId());

        BuyOption buySellUnit = buyOptionService.findById(saved.getId());

        assertEquals(Long.valueOf(saved.getQuantityCupom()-1) , buySellUnit.getQuantityCupom());

    }

    @Test
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
    public void shouldReturnAllValidDeals() {

        BuyOption buyOption = BuyOption.builder()
                .title(faker.lorem().characters(100))
                .startDate(LocalDate.now().minusDays(2))
                .endDate(LocalDate.now().plusDays(20))
                .normalPrice(faker.number().randomDouble(2, 1, 10))
                .percentageDiscount(faker.number().randomDouble(0, 1, 100))
                .salePrice(faker.number().randomDouble(2, 1, 10))
                .quantityCupom(faker.number().randomNumber())
                .build();

        BuyOption buyOption2 = BuyOption.builder()
                .title(faker.lorem().characters(100))
                .startDate(LocalDate.now().plusDays(2))
                .endDate(LocalDate.now().plusDays(20))
                .normalPrice(faker.number().randomDouble(2, 1, 10))
                .percentageDiscount(faker.number().randomDouble(0, 1, 100))
                .salePrice(faker.number().randomDouble(2, 1, 10))
                .quantityCupom(faker.number().randomNumber())
                .build();

        BuyOption buyOption3 = BuyOption.builder()
                .title(faker.lorem().characters(100))
                .startDate(LocalDate.now().plusDays(2))
                .endDate(LocalDate.now().plusDays(20))
                .normalPrice(faker.number().randomDouble(2, 1, 10))
                .percentageDiscount(faker.number().randomDouble(0, 1, 100))
                .salePrice(faker.number().randomDouble(2, 1, 10))
                .quantityCupom(0L)
                .build();

        buyOptionRepository.save(buyOption);
        buyOptionRepository.save(buyOption2);
        buyOptionRepository.save(buyOption3);

        List<BuyOptionDTO> result = buyOptionService.findAllWithPublishDateValid();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
