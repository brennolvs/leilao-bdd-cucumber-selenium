# language: pt

@leilao
Funcionalidade: Cadastrando um leilao

Contexto:
Dado o usuario logado

Cenário: Cadastro de leilão com dados válidos
Quando acessa a pagina de novo leilao
E preenche o formulario com dados validos
Entao volta para a pagina de leiloes
E o novo leilao aparece na tabela