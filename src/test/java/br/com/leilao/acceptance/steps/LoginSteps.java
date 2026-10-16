package br.com.leilao.acceptance.steps;

import org.junit.Assert;

import br.com.leilao.e2e.pages.Browser;
import br.com.leilao.e2e.pages.LeiloesPage;
import br.com.leilao.e2e.pages.LoginPage;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class LoginSteps {

    private Browser browser;
    private LoginPage loginPage;
    private LeiloesPage leiloesPage;

    @Dado("o usuario valido")
    public void o_usuario_valido_consegue_se_logar() {
        browser = new Browser();
        browser.seed();
        this.loginPage = browser.getLoginPage();
    }

    @Quando("realiza login")
    public void realiza_login() {
        this.leiloesPage = this.loginPage.realizaLoginComo("fulano", "pass");
    }

    @Entao("é redirecionado para a pagina de leiloes")
    public void é_redirecionado_para_a_pagina_de_leiloes() {
        Assert.assertTrue(this.leiloesPage.estaNaPaginaDeLeiloes());
        browser.clean();
    }

    @Dado("o usuario invalido")
    public void o_usuario_invalido() {
        browser = new Browser();
        this.loginPage = browser.getLoginPage();
        this.loginPage.realizaLoginComo("fulano", "alfa");
    }

    @Quando("tenta se logarEntao continua na pagina de login")
    public void tenta_se_logar_entao_continua_na_pagina_de_login() {
        Assert.assertTrue(this.loginPage.estaNaPaginaDeLoginComErro());
    }

}
