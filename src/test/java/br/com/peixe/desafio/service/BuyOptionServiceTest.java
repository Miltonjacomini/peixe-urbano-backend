package br.com.peixe.desafio.service;

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

import java.time.LocalDate;

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

        BuyOption buyOption = BuyOption.builder()
                .title(faker.lorem().characters(100))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .normalPrice(faker.number().randomDouble(2, 1, 10))
                .percentageDiscount(faker.number().randomDouble(0, 1, 100))
                .quantityCupom(faker.number().randomNumber())
                .build();

        BuyOption saved = buyOptionService.insert(buyOption);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(Double.valueOf(saved.getSalePrice()), Double.valueOf(buyOption.getNormalPrice() * 100 / buyOption.getPercentageDiscount()));
    }

    @Test
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
    public void shouldSaleBuyOption() {

        Deal deal = Deal.builder()
                .title(faker.lorem().characters(50))
                .createDate(LocalDate.now())
                .publishDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .text(faker.lorem().characters(200))
                .totalSold(Long.valueOf(0))
                .type(TypeDeal.LOC)
                .build();

        BuyOption buyOption = BuyOption.builder()
                .title(faker.lorem().characters(100))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .normalPrice(faker.number().randomDouble(2, 1, 10))
                .percentageDiscount(faker.number().randomDouble(0, 1, 100))
                .quantityCupom(faker.number().randomNumber())
                .build();

        Deal dealSaved = dealService.insert(deal);
        buyOption.setDealId(dealSaved.getId());

        BuyOption saved = buyOptionService.insert(buyOption);

        assertNotNull(saved.getId());


        buyOptionService.sellUnit(saved.getId());

        BuyOption buySellUnit = buyOptionService.findById(saved.getId());

        assertEquals(Long.valueOf(saved.getQuantityCupom()-1) , buySellUnit.getQuantityCupom());

    }


}
