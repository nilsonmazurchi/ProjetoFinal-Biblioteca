# Sistema de Emprestimo de Livros 

Trabalho final de Programação Web II da ADA

## 🚀 Começando

Essas instruções permitirão que você obtenha uma cópia do projeto em operação na sua máquina local para fins de desenvolvimento e teste.

### 📋 Pré-requisitos

Banco de Dados PostgreSQL, Java17, Maven, Spring Boot 3.2.3, Docker-Compose, Docker, Postman e DbGate

### 🔧 Instalação
Vc deve ter instalado o Docker.
Ao realizar o clone do repositorio, entre no diretorio biblioteca e execute:
```
docker-compose -f docker-compose.yml up -d
```
O comando acima deve criar um container docker com postgreSQL.
* Certifique-se que o container esta rodando.
* Em seguida abra o projeto e instale todas as dependencias atraves do Maven.
* Execute o projeto através da BibliotecaApplication.
* Com o DbGate iremos conectar ao banco de dados criado.
* Em uma pasta do repositorio chamada Postman esta o arquivo(biblioteca.postman_collection.json) com as rotas para testar.
* Importe para seu Postman.
* Terá uma aba chamada biblioteca apos a importação.

## ⚙️ Executando os testes

No Postman biblioteca teremos tres conjuntos de pastas:
* Livros
* Emprestimos
* Usuario

Comece pela aba livros:
* Existe dois Post para Cadastrar Lvros Novos - execute-os.
* Observe retorno na aba Response do Postman e verifique no Banco de Dados a criação dos livros.
* Existe também: um Get para Listar todos os livros cadastrados, um Put para fazer Update e um Delete para apgar um Livro.

Depois vá para aba Usuario:
* uma rota Post para cadastrar Usuario com 01 endereço e outra rota Post para cadastrar Usuario com dois ou mais endereços.
(Observe retorno na aba Response do Postman e verifique no Banco de Dados a criação dos usuarios.)
* um Get para Listar todos os Usuarios e seus Endereços, 
* um Put para Fazer Update no usuario, 
* um Put para Fazer Update no Endereço do Usuario por ID, 
* um Post para Criar um novo Endereçono Usuario Existente por ID e Delete para apagar o Endereço do Usuario por ID.
Nosso usuario não será deletado mas sim inativado

Por ultimo dirija-se a aba Emprestimo:
* uma rota Post para Cadastrar Emprestimo por Dados no form-data, 
* um Get para Listar todos os Livros Disponiveis, 
* Um Get para Listar Usuarios com Livros Pendentes,
* um Put para desativar(inativar) um Usuario por ID,
* Um Put para Devolução por ID,
* um Put para Devolução por ID e dataDevolução digitada.(esta para testar retorno quando colocamos uma data de devolução 
superior a data prevista de retorno com calculo da multa)
* um Get para buscar todos os emprestimos em aberto

## 🛠️ Construído com

Mencione as ferramentas que você usou para criar seu projeto

* [Spring Boot](https://spring.io/projects/spring-boot) - O framework web usado
* [Maven](https://maven.apache.org/) - Gerente de Dependência
* [Postman](https://www.postman.com/) - Usada para testar as rotas
* [Docker](https://www.docker.com/) - Usada para construir um ambiente para aplicação
* [PostgreSQL](https://www.postgresql.org/) - Banco de dados
* [DbGate](https://dbgate.org/) - Usada para Gerenciamento do BD
* [Docker-Compose](https://docs.docker.com/compose/install/) - Usada para construir um ambiente para aplicação

## ✒️ Autores

Mencione todos aqueles que ajudaram a levantar o projeto desde o seu início

* *Nilson Mazurchi** - *Trabalho Inicial* - [Nilson Mazurchi](https://github.com/nilsonmazurchi)

## 📄 Licença

Este projeto está sob a licença (MIT) - veja o arquivo [LICENSE.md](https://github.com/nilsonmazurchi/ProjetoFinal-Biblioteca/blob/main/LICENSE) para detalhes.

## 🎁 Expressões de gratidão

* Aos professores [Alex Facincani](https://github.com/facincani) e [Luiz Cardozo](https://github.com/lcrdz)  📢;
* Obrigado por todo o conhecimento transmitido 📋;
* E agora vamos comemorar 🍺;
* E continuar buscando uma oportunidade 🚶;
