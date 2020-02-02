package br.com.peixe.desafio.model.entity;

import br.com.peixe.desafio.model.dto.BuyOptionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "buy_option")
public class BuyOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_buy_option")
    private Long id;

    @Column(name = "deal_id")
    private Long dealId;

    @NotEmpty
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "normal_price", nullable = false)
    private Double normalPrice;

    @Column(name = "sale_price", nullable = false)
    private Double salePrice;

    @NotNull
    @Column(name = "percentage_discount", nullable = false)
    private Double percentageDiscount;

    @NotNull
    @Column(name = "quantity_cupom", nullable = false)
    private Long quantityCupom;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    public BuyOption(BuyOptionDTO buyOptionDTO) {
        this.setId(buyOptionDTO.getId() == null ? null : buyOptionDTO.getId());
        this.setDealId(buyOptionDTO.getDealId() == null ? null : buyOptionDTO.getDealId());
        this.setTitle(buyOptionDTO.getTitle());
        this.setNormalPrice(buyOptionDTO.getNormalPrice());
        this.setSalePrice(buyOptionDTO.getSalePrice());
        this.setPercentageDiscount(buyOptionDTO.getPercentageDiscount());
        this.setQuantityCupom(buyOptionDTO.getQuantityCupom());
        this.setStartDate(buyOptionDTO.getStartDate());
        this.setEndDate(buyOptionDTO.getEndDate());
    }
}
