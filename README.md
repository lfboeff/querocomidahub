# 🍽️ Quero Comida Hub

## 📚 Tech Challenge 1 - Pós-graduação em Arquitetura e Desenvolvimento Java - FIAP 💻

Backend robusto para gerenciamento de usuários em uma plataforma de alimentação. Suporta cadastro diferenciado entre clientes e proprietários de restaurante, autenticação via login e senha, gerenciamento de endereço e troca de senha.

---

## 🛠️ Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.5 |
| Banco de dados | MySQL 8.4 |
| Acesso a dados | Spring JdbcClient (sem ORM) |
| Migrações | Flyway |
| Documentação | SpringDoc OpenAPI 2.8.6 (Swagger UI) |
| Segurança | spring-security-crypto (BCrypt) |
| Containerização | Docker + Docker Compose |

---

## 🚀 Como executar

**Pré-requisitos:** Docker e Docker Compose instalados. Java e Maven **não** são necessários localmente - a aplicação é construída e roda dentro de contêineres.

```bash
# Clonar o repositório
git clone git@github.com:lfboeff/querocomidahub.git
cd querocomidahub

# Build e inicialização dos contêineres
docker compose up --build
```

Com a aplicação em execução, os seguintes endereços ficam disponíveis:

| Recurso | URL |
|---|---|
| Base da API | http://localhost:8080/api/v1 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| Actuator (health) | http://localhost:9090/actuator/health |

```bash
# Encerrar a aplicação
docker compose down

# Encerrar e remover os volumes (limpa o banco de dados)
docker compose down -v
```

---

## 📡 Endpoints

Todos os endpoints utilizam o prefixo `/api/v1` e seguem a semântica REST. Respostas de erro seguem o padrão **RFC 7807** (ProblemDetail).

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/users` | Lista todos os usuários. Suporta filtro opcional `?name=` |
| `POST` | `/users` | Cria um novo usuário com endereço |
| `PUT` | `/users/{id}` | Atualiza dados do usuário (exceto senha e endereço) |
| `DELETE` | `/users/{id}` | Remove permanentemente um usuário e seu endereço |
| `PUT` | `/users/{id}/address` | Substitui o endereço de um usuário |
| `PATCH` | `/users/{id}/password` | Altera a senha (exige a senha atual) |
| `POST` | `/users/login` | Valida as credenciais e retorna o perfil do usuário |

> Exemplos completos de requisição e resposta estão disponíveis na **Swagger UI** e na **coleção Postman**.

---

## 🗂️ Coleção Postman

Uma coleção pronta para importação está disponível em:

```
postman/querocomidahub.postman_collection.json
```

Contém **11 cenários de sucesso** (Happy Path) e **23 cenários de erro** (Error Path), pré-configurados para `http://localhost:8080/api/v1`. Compatível com Postman, Bruno e Insomnia.

---

## 📁 Estrutura do Projeto

```
querocomidahub/
├── src/main/java/br/com/fiap/querocomidahub/
│   ├── controller/       # UserController + interface UserApi (Swagger)
│   ├── service/          # UserService (interface) + UserServiceImpl
│   ├── repository/       # UserRepository + AddressRepository (interfaces) + suas implementações
│   ├── domain/           # User, Address, UserType
│   ├── dto/              # DTOs de request e response
│   ├── mapper/           # UserMapper (utilitário estático)
│   ├── exception/        # Exceções de domínio + GlobalExceptionHandler
│   └── config/           # BCryptPasswordEncoder + OpenAPI config
├── src/main/resources/
│   └── db/migration/     # Migrações Flyway (V0–V3)
├── postman/              # Coleção Postman
├── Dockerfile
├── docker-compose.yml
├── docker-compose.override.yml   # Exposição de portas (ambiente local)
├── .env.example          # Template de variáveis de ambiente
└── .env                  # Variáveis de ambiente para Docker Compose (fins acadêmicos)
```

---

## 👤 Autor

| Nome | RM |
|---|---|
| Luís Felipe Boeff | RM372311 |
