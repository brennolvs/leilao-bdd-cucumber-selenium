# language: pt

Funcionalidade: Apenas usuarios cadastrados podem se logar

Cenario: O usuario valido consegue se logar
Dado o usuario valido 
Quando realiza login
Entao é redirecionado para a pagina de leiloes

Cenario: Um usuario invalido nao consegue logar
Dado o usuario invalido
Quando tenta se logarEntao continua na pagina de login