package br.com.peixe.desafio.model.entity;

public enum TypeDeal {

    LOC("Local"),
    PRO("Produto"),
    VIA("Viagem");

    private String descricao;

    TypeDeal(String descricao) {
        this.descricao = descricao;
    }

}
