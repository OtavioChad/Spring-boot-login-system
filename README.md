# Spring Boot HelpDesk Ticket System

Sistema web de gerenciamento de tickets desenvolvido com **Spring Boot**.

O objetivo do projeto foi praticar conceitos importantes de desenvolvimento backend com **Java**, incluindo autenticação de usuários, controle de acesso, persistência de dados, organização em camadas e integração com banco de dados.

O sistema permite que usuários criem tickets de suporte, enviem mensagens dentro do ticket e acompanhem seu status, enquanto administradores podem gerenciar tickets e usuários.

---

# 📸 Screenshots

### Login

<img src="images/login.JPEG" width="700"/>

### Cadastro

<img src="images/cadastro.JPEG" width="700"/>

### Sistema de Tickets

<img src="images/ticket.JPEG" width="700"/>

### Painel de Administração

<img src="images/admin-usuarios.JPEG" width="700"/>

---

# Tecnologias Utilizadas

* Java
* Spring Boot
* Spring MVC
* Spring Data JPA
* MySQL
* Thymeleaf
* BCrypt (criptografia de senha)
* Maven
* HTML / CSS / JavaScript

---

# Funcionalidades

## Autenticação

* Cadastro de usuário
* Login com email e senha
* Senha criptografada com BCrypt
* Controle de sessão utilizando cookies
* Logout do sistema
* Interceptor bloqueando acesso a páginas protegidas

## Usuário

* Visualizar dashboard
* Editar perfil
* Alterar senha
* Criar tickets
* Visualizar seus próprios tickets
* Conversar dentro do ticket

## Sistema de Tickets

* Criar ticket
* Visualizar tickets
* Enviar mensagens dentro do ticket
* Atualização de status do ticket
* Histórico de mensagens no ticket

### Status possíveis

* **ABERTO**
* **EM_ANALISE**
* **FINALIZADO**

## Administração

* Visualizar todos os usuários
* Gerenciar usuários
* Painel administrativo

---

# 📁 Estrutura do Projeto

```
src/main/java
│
└── SpringSystemLogin
    │
    ├── SpringSystemLoginApplication.java
    │
    ├── config
    │   └── SecurityConfig.java
    │
    ├── controller
    │   ├── LoginController.java
    │   └── TicketController.java
    │
    ├── database
    │   └── JDBCserver.java
    │
    ├── model
    │   ├── Usuario.java
    │   ├── Ticket.java
    │   ├── TicketMensagem.java
    │   └── TicketStatus.java
    │
    ├── repository
    │   ├── UsuarioRepository.java
    │   ├── TicketRepository.java
    │   └── TicketMensagemRepository.java
    │
    ├── service
    │   └── CookieService.java
    │
    └── serviceInterceptor
        ├── LoginInterceptor.java
        └── LoginInterceptorAppConfig.java
```

---

# 📂 Templates (Thymeleaf)

```
src/main/resources/templates
│
├── login.html
├── cadastro.html
├── index.html
├── perfil.html
├── configs.html
├── tickets.html
└── adminUsuarios.html
```

---

# 🎨 Arquivos Estáticos

```
src/main/resources/static
│
├── css
│   └── styles.css
```

---

# Organização das camadas

* **controller** → controla as rotas da aplicação
* **model** → entidades do banco de dados
* **repository** → acesso aos dados com Spring Data JPA
* **service** → serviços utilitários e lógica auxiliar
* **serviceInterceptor** → controle de autenticação nas rotas
* **config** → configurações da aplicação
* **database** → configuração da conexão com o banco

---

# Banco de Dados

O sistema utiliza **MySQL**.

Crie um banco com o nome:

```
loginapp
```

A conexão com o banco está configurada na classe:

```
JDBCserver
```

---

# Como executar o projeto

### 1. Clonar o repositório

```
git clone https://github.com/OtavioChad/Spring-boot-login-system.git
```

### 2. Abrir o projeto no **Eclipse ou IntelliJ**

### 3. Configurar as credenciais do banco MySQL na classe

```
JDBCserver
```

### 4. Executar a classe principal

```
SpringSystemLoginApplication.java
```

### 5. Acessar no navegador

```
http://localhost:8080/login
```

---

# Objetivo do Projeto

Esse projeto foi desenvolvido para praticar desenvolvimento backend com **Java e Spring Boot**, focando em:

* Autenticação
* Controle de acesso
* CRUD com banco de dados
* Arquitetura MVC
* Integração com MySQL
* Organização de projeto Spring

---

# Autor

**Otávio Chad**
