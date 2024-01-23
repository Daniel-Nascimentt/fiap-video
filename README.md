# FIAP VIDEO - Tech challenge Arquitetura e desenvolvimento Java - Fase4

# ÍNDICE

* [Tecnologias](#tecnologias)
* [Start](#start)
* [Decisoes](#decisoes)
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

# Fluxos principais (Usuario e Sistema) 👣 :
![image](https://github.com/Daniel-Nascimentt/fiap-video/assets/65513073/a1c38a60-0154-4008-bbb0-78c7b5094d72)



![image](https://github.com/Daniel-Nascimentt/fiap-video/assets/65513073/14136ee7-9f7c-4c65-b0c9-64d6fc47d2ed)
#

# Swagger

Link para swagger:
> http://localhost:8080/fiap-video/swagger-ui/index.html#/


