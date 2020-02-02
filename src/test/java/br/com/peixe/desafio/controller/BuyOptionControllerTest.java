package br.com.peixe.desafio.controller;

import br.com.peixe.desafio.model.dto.BuyOptionDTO;
import br.com.peixe.desafio.model.dto.DealDTO;
import br.com.peixe.desafio.model.entity.BuyOption;
import br.com.peixe.desafio.model.entity.Deal;
import br.com.peixe.desafio.model.entity.TypeDeal;
import br.com.peixe.desafio.repository.BuyOptionRepository;
import br.com.peixe.desafio.repository.DealRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BuyOptionControllerTest {

    protected final Faker faker = Faker.instance();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BuyOptionRepository buyOptionRepository;

    @MockBean
    private DealRepository dealRepository;

    @Test
    public void shouldSaveBuyOption() throws Exception {

        Long idBuyOption = faker.random().nextLong(5);

        BuyOptionDTO buyOptionDTO = BuyOptionDTO.builder()
                .id(idBuyOption)
                .title(faker.lorem().characters(100))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .normalPrice(faker.number().randomDouble(2, 1, 10))
                .percentageDiscount(faker.number().randomDouble(0, 1, 100))
                .quantityCupom(faker.number().randomNumber())
                .build();

        BuyOption buyOption = new BuyOption(buyOptionDTO);

        when(buyOptionRepository.save(any(BuyOption.class))).thenReturn(buyOption);

        mockMvc.perform(post(BuyOptionController.POST_BUY_OPTION)
        .content(objectMapper.writeValueAsString(buyOptionDTO))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(idBuyOption.intValue())));
    }

    @Test
    public void shouldSellUnit() throws Exception {

        Long idBuyOption = faker.random().nextLong(5);
        Long idDeal = faker.random().nextLong(5);

        DealDTO dealDTO = DealDTO.builder()
                .id(idDeal)
                .title(faker.lorem().characters(50))
                .createDate(LocalDate.now())
                .publishDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .text(faker.lorem().characters(200))
                .totalSold(Long.valueOf(0))
                .type(TypeDeal.LOC)
                .buyOptions(Collections.emptyList())
                .build();

        BuyOptionDTO buyOptionDTO = BuyOptionDTO.builder()
                .id(idBuyOption)
                .title(faker.lorem().characters(100))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .normalPrice(faker.number().randomDouble(2, 1, 10))
                .percentageDiscount(faker.number().randomDouble(0, 1, 100))
                .quantityCupom(faker.number().randomNumber())
                .dealId(idDeal)
                .build();

        BuyOption buyOption = new BuyOption(buyOptionDTO);

        when(buyOptionRepository.findById(eq(idBuyOption))).thenReturn(Optional.of(buyOption));
        when(buyOptionRepository.save(any(BuyOption.class))).thenReturn(buyOption);

        when(dealRepository.findById(eq(idDeal))).thenReturn(Optional.of(new Deal(dealDTO)));

        mockMvc.perform(post(BuyOptionController.POST_SELL_UNIT.replace("{buyOptionId}", String.valueOf(idBuyOption)))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is("Unidade vendida com sucesso!!")));

    }

}
