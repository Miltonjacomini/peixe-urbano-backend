package br.com.peixe.desafio.models.entity;

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
    @Column(name= "id_buy_option")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "normal_price", nullable = false)
    private Double normalPrice;

    @NotNull
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

}
