package br.com.peixe.desafio.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TechServiceTest {

    private TechService techService = new TechService();

    @Test
    public void testeNomeSobrenomeComTechParaRH() {

        String resultado = techService.modificaNome("Milton", "Jacomini");

        assertEquals("Milton Jacomini - TechParaRH", resultado);

    }

}
