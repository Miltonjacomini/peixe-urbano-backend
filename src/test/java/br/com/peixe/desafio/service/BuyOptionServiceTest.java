package br.com.peixe.desafio.service;

import br.com.peixe.desafio.model.dto.BuyOptionDTO;
import br.com.peixe.desafio.model.dto.DealDTO;
import br.com.peixe.desafio.model.entity.BuyOption;
import br.com.peixe.desafio.model.entity.Deal;
import br.com.peixe.desafio.model.entity.TypeDeal;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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


}
