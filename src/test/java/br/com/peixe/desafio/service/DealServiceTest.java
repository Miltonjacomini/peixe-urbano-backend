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

import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "/sql/clearDB.sql",
        executionPhase = AFTER_TEST_METHOD,
        config = @SqlConfig(transactionMode = ISOLATED))
public class DealServiceTest {

    protected final Faker faker = Faker.instance();

    @Autowired
    private DealService dealService;

    @Autowired
    private BuyOptionService buyOptionService;

    @Test
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
    public void shouldBeValidDeal() {

        DealDTO deal = DealDTO.builder()
                .title(faker.lorem().characters(50))
                .createDate(LocalDate.now())
                .publishDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .text(faker.lorem().characters(200))
                .totalSold(Long.valueOf(0))
                .type(TypeDeal.LOC)
                .build();

        DealDTO saved = dealService.insert(deal);

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotValidDealPublishDateBeforeToday() {

        DealDTO deal = DealDTO.builder()
                .title(faker.lorem().characters(50))
                .createDate(LocalDate.now())
                .publishDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(20))
                .text(faker.lorem().characters(200))
                .totalSold(Long.valueOf(0))
                .type(TypeDeal.LOC)
                .build();

        DealDTO saved = dealService.insert(deal);

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotValidDealEndDateAfterToday() {

        DealDTO deal = DealDTO.builder()
                .title(faker.lorem().characters(50))
                .createDate(LocalDate.now())
                .publishDate(LocalDate.now())
                .endDate(LocalDate.now().minusDays(20))
                .text(faker.lorem().characters(200))
                .totalSold(Long.valueOf(0))
                .type(TypeDeal.LOC)
                .build();

        DealDTO saved = dealService.insert(deal);

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
    public void shouldUpdateDealWithBuyOption() {

        BuyOptionDTO buyOption = BuyOptionDTO.builder()
                .title(faker.lorem().characters(100))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .normalPrice(faker.number().randomDouble(2, 1, 10))
                .percentageDiscount(faker.number().randomDouble(0, 1, 100))
                .quantityCupom(faker.number().randomNumber())
                .build();

        DealDTO deal = DealDTO.builder()
                .title(faker.lorem().characters(50))
                .createDate(LocalDate.now())
                .publishDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(20))
                .text(faker.lorem().characters(200))
                .totalSold(Long.valueOf(0))
                .type(TypeDeal.LOC)
                .build();

        DealDTO saved = dealService.insert(deal);
        BuyOptionDTO buyOptionSaved = buyOptionService.insert(buyOption);

        DealDTO savedWithBuyOption = dealService.addOption(saved.getId(), buyOptionSaved);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(savedWithBuyOption.getBuyOptions().size(), 1);
    }

    @Test
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
    public void shouldUpdateDealTotalSold() {

        BuyOptionDTO buyOptionDTO = BuyOptionDTO.builder()
                .title(faker.lorem().characters(100))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .normalPrice(faker.number().randomDouble(2, 1, 10))
                .percentageDiscount(faker.number().randomDouble(0, 1, 100))
                .quantityCupom(faker.number().randomNumber())
                .build();

        DealDTO deal = DealDTO.builder()
                .title(faker.lorem().characters(50))
                .createDate(LocalDate.now())
                .publishDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(20))
                .text(faker.lorem().characters(200))
                .totalSold(Long.valueOf(0))
                .type(TypeDeal.LOC)
                .build();

        DealDTO saved = dealService.insert(deal);
        buyOptionDTO.setDealId(saved.getId());
        BuyOptionDTO buyOptionSaved = buyOptionService.insert(buyOptionDTO);

        DealDTO savedWithBuyOption = dealService.addOption(saved.getId(), buyOptionSaved);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(1, savedWithBuyOption.getBuyOptions().size());

        dealService.updateTotalSold(buyOptionSaved);

        Deal updatedTotalSold = dealService.findById(deal.getId());

        assertNotEquals(saved.getTotalSold(), updatedTotalSold.getTotalSold());
    }
}
