# FIAP VIDEO - Tech challenge Arquitetura e desenvolvimento Java - Fase4

# ÍNDICE

* [Tecnologias](#tecnologias)
* [Start](#start)
* [Sobre o projeto](#sobreOProjeto)
* [Fluxos principais (Usuario e Sistema)](#fluxosPrincipaisUsuarioESistema)
* [Cobertura de testes de unidade](#coberturaDeTestesDeUnidade)
* [Testes integrados](#testesIntegrados)
* [Swagger](#swagger)
* [Postman Collection](#postmanCollection)

# 

# Tecnologias 👨🏻‍💻 

* Setup
    * Java 17 
    * Spring boot 3.2.0
    * DevTools
    * Lombok 
    * Spring Web
    * Open API 
    * Spring Data Reactive Mongodb
    * Bean Validation
    * WebFlux
    * TestContainers
* GIT 
* IDE's (Intellij, VS Code)
* Postman
* Mongo Compass
* Docker

#

# Start 👨🏻‍🔧

1º Faça o clone do projeto.

2º Gere o .jar executando o comando (Pulando os testes para ser mais rapido, mas fique avontade para retirar o "-DskipTests"):
> mvn clean install -DskipTests

3º Execute o seguinte comando para subir a infra do projeto:
>  docker-compose up

#

# Sobre o projeto

Foi proposto um desafio de criação de uma API de streaming de videos para a 4ª fase do tech challenge da POS Tech da FIAP em Arquitetura e Desenvolvimento Java.

Dentre os requisitos, estavam presentes o desenvolvimento reativo com Spring Web Flux, o uso de desenvolvimento em camadas com o Clean Architecture, testes de unidade e integração, etc.

Com isso fiz a criação de uma API que atendesse esses mesmos requisitos, implementando tambem cenários que contem a inversão de dependencia, regras de negócio mais "centralizadas" e o uso de algumas abstrações para facilidade de implementação de novas funcionalidades em alguns pontos da API que era passivel de novas implementações mais elaboradas.

### WebFlux

Falando sobre a implementação reativa do projeto, o ponto que faz o "subscribe", ou seja, que ativa o fluxo reativo está implementado na borda mais externa da API, os controllers. Dessa forma não há a necessidade de ativar esses fluxos manualmente nas camadas interiores da aplicação. Existe uma exceção que é em uma camada onde não é acessivel via controller, na implemntação desse projeto seria nos fluxos relacionados a conta do usuário, dessa forma é necessario fazer essa ativação manualmente.

### Clean Architecture

Para seguir alguns padrões, for organizado a aplicação pensando nessas camadas da clean architecture. A representatividade do gateway seria a camada de serviço *(@Service)* da API. Pois essa camada está sendo responsável por acionar os use cases fornecendo as dependencias necessárias para executar alguma lógica de negócio. Sendo assim, somente os Services acessam o UseCases e os UseCases acessam a camada de dominio. Sendo assim, não existe nesse projeto um acesso direto a camada de dominio da aplicação, somente os use cases as acessam diretamente.

### Inversão de dependencia

Como os Use cases executam as logicas de negócio, muitas vezes há a necessidade de acesso a base de dados, sendo assim, o gateway/*(@Service)* é responsavel por instanciar o repository necessário que o UseCase vai precisar para executar sua lógica, dessa forma removendo a responsabilidade do UseCase gerenciar essa dependencia de acesso e manipulção de dados do banco de dados.

### Testes de unidade

Foi implementado também os testes de unidade validando todas as camadas da API, desde controllers até os UseCases que interagem com a camada de dominio da aplicação. É possivel executa-los a partir do comando:
> mvn clean install 

### Testes de integração

Foi implementados testesde integração nas camadas de Controller e Service usando o *@TestContainers*. É possivel executa-los a partir do comando:
> mvn clean install -P integration-tests

### Desenhos de fluxos

Criar os desenhos deu mais clareza do que precisava ser feito.

### Filtros de videos dinamicos

Um dos requisitos tecnicos, era de implementar filtros por categoria, data de publicação e titulo. Com isso foi implementado um filtro dinamico com todos os atributos que compõem um video. Sendo possivel tambem combinar esses filtros para uma busca mais especifica.

### Uso do docker-compose

Visando a facilidade de subir o projeto, foi criado o docker-compose para subir dois containers, o container da app e do mongodb.


#

# Fluxos principais (Usuario e Sistema) 👣 :
![image](https://github.com/Daniel-Nascimentt/fiap-video/assets/65513073/a1c38a60-0154-4008-bbb0-78c7b5094d72)



![image](https://github.com/Daniel-Nascimentt/fiap-video/assets/65513073/14136ee7-9f7c-4c65-b0c9-64d6fc47d2ed)
#

# Cobertura de testes de unidade

![image](https://github.com/Daniel-Nascimentt/fiap-video/assets/65513073/f14adceb-60b4-4763-825e-e7ff3ee5f926)


#

# Testes integrados

![image](https://github.com/Daniel-Nascimentt/fiap-video/assets/65513073/ef929d85-6420-4b20-8c77-a671c35b48d6)

#

# Swagger

Link para swagger:
> http://localhost:8080/fiap-video/swagger-ui/index.html#/

#

# Postman Collection

