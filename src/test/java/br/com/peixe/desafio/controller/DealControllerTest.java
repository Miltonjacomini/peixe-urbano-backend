package br.com.peixe.desafio.controller;

import br.com.peixe.desafio.model.dto.BuyOptionDTO;
import br.com.peixe.desafio.model.dto.DealDTO;
import br.com.peixe.desafio.model.entity.BuyOption;
import br.com.peixe.desafio.model.entity.Deal;
import br.com.peixe.desafio.model.entity.TypeDeal;
import br.com.peixe.desafio.service.BuyOptionService;
import br.com.peixe.desafio.service.DealService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class DealControllerTest {

    protected final Faker faker = Faker.instance();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DealService dealService;

    @MockBean
    private BuyOptionService buyOptionService;

    @Test
    public void shouldSaveDeal() throws Exception {

        long idDeal = faker.random().nextLong(5);

        DealDTO dealDTO = DealDTO.builder()
                .id(idDeal)
                .title(faker.lorem().characters(50))
                .createDate(LocalDate.now())
                .publishDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .text(faker.lorem().characters(200))
                .totalSold(Long.valueOf(0))
                .type(TypeDeal.LOC)
                .build();

        when(dealService.insert(any(DealDTO.class))).thenReturn(dealDTO);

        mockMvc.perform(post(DealController.POST_DEAL)
                .content(objectMapper.writeValueAsString(dealDTO))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dealDTO.getId().intValue())));

        verify(dealService, times(1)).insert(any(DealDTO.class));
    }

    @Test
    public void shouldAddOptionToDeal() throws Exception {

        long idDeal = faker.random().nextLong(5);
        long idBuyOption = faker.random().nextLong(5);

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
                .build();

        when(dealService.findById(eq(idDeal))).thenReturn(new Deal(dealDTO));
        when(buyOptionService.findById(eq(idDeal))).thenReturn(new BuyOption(buyOptionDTO));

        mockMvc.perform(post(DealController.POST_DEAL_ADD_OPTION
                                                .replace("{dealId}", String.valueOf(idDeal))
                                                .replace("{buyOptionId}", String.valueOf(idBuyOption)))

                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Opção adicionada com sucesso!")));

        verify(dealService, times(1)).addOption(eq(idDeal), eq(idBuyOption));
    }
}
