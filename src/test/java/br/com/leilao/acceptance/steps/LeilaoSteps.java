package br.com.leilao.acceptance.steps;

import org.junit.Assert;

import br.com.leilao.e2e.pages.Browser;
import br.com.leilao.e2e.pages.LeiloesPage;
import br.com.leilao.e2e.pages.LoginPage;
import br.com.leilao.e2e.pages.NovoLeilaoPage;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class LeilaoSteps {

    private LoginPage loginPage;
    private LeiloesPage leiloesPage;
    private NovoLeilaoPage novoLeilaoPage;

    @Dado("o usuario logado")
    public void o_usuario_logado() {
        Browser browser = new Browser();
        browser.seed();
        this.loginPage = browser.getLoginPage();
        this.leiloesPage = this.loginPage.realizaLoginComoFulano();
    }

    @Quando("acessa a pagina de novo leilao")
    public void acessa_a_pagina_de_novo_leilao() {
        this.novoLeilaoPage = this.leiloesPage.visitaPaginaParaCriarUmNovoLeilao();
    }

        @Quando("preenche o formulario com dados validos")
    public void preenche_o_formulario_com_dados_validos() {
        this.novoLeilaoPage.preencheForm("PC Antigo", "1500", "01/11/2020");
    }

    @Entao("volta para a pagina de leiloes")
    public void volta_para_a_pagina_de_leiloes() {
        Assert.assertTrue(this.leiloesPage.estaNaPaginaDeLeiloes());
    }

    @Entao("o novo leilao aparece na tabela")
    public void o_novo_leilao_aparece_na_tabela() {
        Assert.assertTrue(this.leiloesPage.existe("PC Antigo", "1500", "01/11/2020", "fulano"));
    }

}
