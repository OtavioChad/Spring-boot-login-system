# Spring System Login

Esse é um projeto simples de sistema de login feito com **Spring Boot**.  
Criei esse projeto para praticar alguns conceitos importantes do desenvolvimento backend com Java, como autenticação, persistência de dados e organização de um projeto Spring.

O sistema permite cadastrar usuários, fazer login e acessar uma página protegida após a autenticação.

---

## Tecnologias usadas

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- MySQL
- Thymeleaf
- BCrypt para criptografia de senha
- Maven

---

## Funcionalidades

- Cadastro de usuário
- Login com email e senha
- Senha criptografada com BCrypt
- Persistência dos usuários no banco MySQL
- Controle de acesso utilizando cookies
- Interceptor para bloquear páginas quando o usuário não está logado
- Logout do sistema

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
    │   └── LoginController.java
    │
    ├── model
    │   └── Usuario.java
    │
    ├── repository
    │   └── UsuarioRepository.java
    │
    ├── service
    │   ├── CookieService.java
    │   │
    │   └── autenticator
    │       ├── LoginInterceptor.java
    │       └── LoginInterceptorAppConfig.java
    │
    └── JDBCserver.java

Principais classes:

- **LoginController** – controla as rotas de login, cadastro e logout
- **Usuario** – entidade que representa o usuário no banco
- **UsuarioRepository** – responsável por acessar os dados dos usuários
- **CookieService** – utilitário para manipular cookies
- **LoginInterceptor** – bloqueia acesso a páginas sem autenticação
- **SecurityConfig** – configuração do encoder de senha
- **JDBCserver** – configuração da conexão com o banco

---

## Banco de dados

O sistema utiliza **MySQL**.

Crie um banco com o nome:

```
loginapp
```

A conexão com o banco está configurada na classe `JDBCserver`.

---

## Como executar o projeto

1. Clonar o repositório

```
git clone https://github.com/otaviochad/Spring-boot-login-system.git
```

2. Abrir o projeto no Eclipse ou IntelliJ

3. Configurar o banco MySQL na classe `JDBCserver`

4. Executar a classe principal

```
SpringSystemLoginApplication.java
```

5. Acessar no navegador

```
http://localhost:8080/login
```

---

## Observação

Esse projeto foi feito principalmente para estudo e prática com **Spring Boot**, principalmente na parte de autenticação básica e organização de um backend Java.
