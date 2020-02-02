package br.com.peixe.desafio.model.dto;

import br.com.peixe.desafio.model.entity.BuyOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BuyOptionDTO {

    private Long id;
    private Long dealId;
    private String title;
    private Double normalPrice;
    private Double salePrice;
    private Double percentageDiscount;
    private Long quantityCupom;
    private LocalDate startDate;
    private LocalDate endDate;


    public BuyOptionDTO(BuyOption buyOption) {
        this.setId(buyOption.getId() == null ? null : buyOption.getId());
        this.setDealId(buyOption.getDealId() == null ? null : buyOption.getDealId());
        this.setTitle(buyOption.getTitle());
        this.setNormalPrice(buyOption.getNormalPrice());
        this.setSalePrice(buyOption.getSalePrice());
        this.setPercentageDiscount(buyOption.getPercentageDiscount());
        this.setQuantityCupom(buyOption.getQuantityCupom());
        this.setStartDate(buyOption.getStartDate());
        this.setEndDate(buyOption.getEndDate());
    }

    public Double getSalePriceCalculated() {
        return this.getNormalPrice() - Double.valueOf(this.getNormalPrice() * this.getPercentageDiscount());
    }
}
