package br.com.peixe.desafio.model.entity;

import br.com.peixe.desafio.model.dto.DealDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "deal")
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_deal")
    private Long id;

    @NotEmpty
    @Column(name = "title", nullable = false)
    private String title;

    @NotEmpty
    @Column(name = "text", nullable = false)
    private String text;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @NotNull
    @Column(name = "publish_date", nullable = false)
    private LocalDate publishDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "url")
    private String url;

    @Column(name = "total_sold")
    private Long totalSold;

    @Enumerated(EnumType.STRING)
    private TypeDeal type;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "deal_option",
            joinColumns = { @JoinColumn(name = "id_deal")},
            inverseJoinColumns = { @JoinColumn(name = "id_buy_option")})
    private List<BuyOption> buyOptions = new ArrayList<>();

    public Deal(DealDTO dealDTO) {
        this.setId(dealDTO.getId());
        this.setCreateDate(dealDTO.getCreateDate());
        this.setPublishDate(dealDTO.getPublishDate());
        this.setEndDate(dealDTO.getEndDate());
        this.setTitle(dealDTO.getTitle());
        this.setText(dealDTO.getText());
        this.setType(dealDTO.getType());
        this.setTotalSold(dealDTO.getTotalSold() == null ? 0L : dealDTO.getTotalSold());
        this.setBuyOptions(dealDTO.getBuyOptions().isEmpty() ? Collections.emptyList() : dealDTO.getBuyOptions().stream().map(BuyOption::new).collect(toList()));
        this.setUrl(dealDTO.getUrl());
    }

}
