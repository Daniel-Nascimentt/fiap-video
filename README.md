# FIAP VIDEO - Tech challenge Arquitetura e desenvolvimento Java - Fase4

# ÍNDICE

* [Tecnologias](#tecnologias)
* [Start](#start)
* [Decisoes](#decisoes)
* [Fluxos principais](#fluxosPrincipais)
* [Swagger](#swagger)
* [Postman Collection](#postmanCollection)

# 

# Tecnologias 👨🏻‍💻 

* Arquitetura: Micro services
* Dependencias
    * Java 17 (Padrão Spring Initializr)
    * Spring boot 3.1.5 (Padrão Spring Initializr)
    * DevTools (Facilitar setup no ambiente de desenvolvimento dando Restart no servidor a cada modificação feita)
    * Lombok (Facilitar criação de métodos acessores e construtores quando necessário)
    * Spring Web (Para usar uma API REST)
    * Open API (Habilitar Swagger)
    * Spring Data Mongo db
    * Bean Validation (Para fazer validações de campos na borda mais externa da API, as REQUESTS)
    * Open Feign (Comunicação entre micro serviços)
    * RabbitMq (Uso de menssageria)
    * Java Mail sender (Envio de e-mails)
* GIT (Controle de versão do projeto)
* IDE's (Intellij, VS Code)
* Postman (Testes da API)
* Mongo Compass (Client MongoDb)
* Docker (Para subir um container com rabbit MQ) - docker run -it  --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

#

# Start 👨🏻‍🔧

1º Faça o clone do projeto.

2º Execute o seguinte comando para subir a infra do projeto:
>  docker-compose up

#

# Fluxos principais do usuário:
![image](https://github.com/Daniel-Nascimentt/fiap-video/assets/65513073/a1c38a60-0154-4008-bbb0-78c7b5094d72)
#

# Funcionalidades da API:
![image](https://github.com/Daniel-Nascimentt/fiap-video/assets/65513073/14136ee7-9f7c-4c65-b0c9-64d6fc47d2ed)
#


