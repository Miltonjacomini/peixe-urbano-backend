package br.com.peixe.desafio.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

@Getter
@JsonSerialize(using = TypeDealSerializer.class)
@JsonDeserialize(using = TypeDealDeserializer.class)
public enum TypeDeal {

    LOC("Local"),
    PRO("Produto"),
    VIA("Viagem");

    private String descricao;

    TypeDeal(String descricao) {
        this.descricao = descricao;
    }

}
