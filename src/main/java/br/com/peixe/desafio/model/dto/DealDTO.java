package br.com.peixe.desafio.model.dto;

import br.com.peixe.desafio.model.entity.Deal;
import br.com.peixe.desafio.model.entity.TypeDeal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DealDTO {

    private Long id;
    private String title;
    private String text;
    private LocalDate createDate;
    private LocalDate publishDate;
    private LocalDate endDate;
    private String url;
    private Long totalSold;
    private TypeDeal type;
    private List<BuyOptionDTO> buyOptions = new ArrayList<>();

    public DealDTO(Deal deal) {
        this.setId(deal.getId());
        this.setCreateDate(deal.getCreateDate());
        this.setPublishDate(deal.getPublishDate());
        this.setEndDate(deal.getEndDate());
        this.setTitle(deal.getTitle());
        this.setText(deal.getText());
        this.setType(deal.getType());
        this.setTotalSold(deal.getTotalSold());
        this.setBuyOptions(deal.getBuyOptions().isEmpty() ? Collections.emptyList() : deal.getBuyOptions().stream().map(BuyOptionDTO::new).collect(toList()));
        this.setUrl(deal.getUrl());
    }
}
