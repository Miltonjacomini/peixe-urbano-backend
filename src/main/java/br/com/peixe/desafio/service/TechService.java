package br.com.peixe.desafio.service;

public class TechService {

    public String modificaNome(String nome, String sobrenome) {

        String resultado = nome.concat(" ").concat(sobrenome).concat(" - TechParaRH");

        return resultado;
    }
}
