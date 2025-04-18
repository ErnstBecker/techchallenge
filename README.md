# Techchallenge

## Sobre
Este projeto é uma API REST que fornece informações sobre as criptomoedas com maior valor de mercado. A aplicação utiliza a API do Coinpaprika para obter dados de criptomoedas, armazena-os em cache usando Redis, e oferece um endpoint para consultar as top criptomoedas por valor de mercado. Também fornece funcionalidade de conversão de moeda usando a API Currencylayer, permitindo que os usuários vejam os valores em diferentes moedas fiduciárias.

## 1. Instruções para Rodar o Projeto com Docker

### Pré-requisitos
- Git
- Docker e Docker Compose instalados
- Acesso à internet para download das imagens Docker e acesso às APIs externas

### Passos para Execução
1. Execute a aplicação usando Docker Compose:
   ```bash
   docker-compose up -d
   ```
   
   Isso iniciará dois containers:
   - Redis na porta 6379
   - Aplicação Spring Boot na porta 8080

2. A aplicação estará disponível em:
   ```
   http://localhost:8080
   ```

3. Para parar a aplicação:
   ```bash
   docker-compose down
   ```

## 2. Endpoints da Aplicação

### Obter Top Criptomoedas
```
GET http:/localhost:8080/rest/v1/magic-coins
```

**Parâmetros de consulta:**
- `currency` (opcional): Código da moeda para conversão de preços (padrão: "USD")
- `top` (opcional): Número de criptomoedas para retornar (padrão: 10, mín: 1, máx: 50)

**Exemplo de requisição:**
```
GET http:/localhost:8080/rest/v1/magic-coins?currency=BRL&top=10
```

**Exemplo de resposta:**
```json
[
  {
    "name": "Bitcoin",
    "symbol": "BTC",
    "rank": 1,
    "quote": {
      "USD": 62458.73,
      "BRL": 318528.95
    }
  },
  {
    "name": "Ethereum",
    "symbol": "ETH",
    "rank": 2,
    "quote": {
      "USD": 3049.21,
      "BRL": 15550.97
    }
  },
  ...
]
```

**Códigos de resposta:**
- 200: Sucesso
- 400: Requisição contém dados inválidos
- 500: Erro interno do servidor

## 3. Pontos de Melhoria

### 3.1. TTL do Cache como Variável de Ambiente
Atualmente, o TTL do cache está definido como 1 minuto hardcoded na classe `HandleRedisCacheAdapter`. Seria mais flexível e seguiria melhores práticas definir esse valor como uma variável de ambiente, permitindo ajustes sem necessidade de recompilação.

### 3.2. Adicionar Retryable para API de Conversão
A API de conversão (Currencylayer) pode ocasionalmente falhar ou retornar valores zerados quando o campo `success` da resposta é falso. Implementar um mecanismo de retry com backoff exponencial melhoraria a resiliência do sistema.

#### 3.2.1. Tratamento para Casos de Sucesso Falso
Atualmente não há tratamento específico quando o campo `success` da resposta de conversão é falso, o que pode resultar em valores zerados ou incorretos sendo apresentados ao usuário.

### 3.3. Verificar Disponibilidade do Redis
A aplicação atualmente falha quando o Redis não está disponível. Implementar um mecanismo de verificação de saúde do Redis e fallback gracioso (como utilizar dados não cacheados) melhoraria a robustez da aplicação.

### 3.4. Validar o Currency Enviado pelo Usuário
Não existe validação para o parâmetro `currency` recebido do usuário. Adicionar validação contra uma lista de moedas suportadas evitaria erros na API de conversão e melhoraria a experiência do usuário.

### 3.5. Utilizar Mappers e DTOs
Seguindo melhor os princípios da arquitetura hexagonal, seria recomendável separar os objetos de domínio dos DTOs usados na comunicação com sistemas externos. Atualmente, todas as classes estão no pacote `domain.models`.

### 3.6. Aumentar a cobertura de testes
A cobertura de testes está abaixo do ideal. Seria recomendável aumentar a cobertura dos testes unitários para garantir maior confiabilidade, facilitar refatorações, e validar casos.
(BONUS) Adicionar testes End-to-End.

## 4. Bibliotecas Utilizadas

### Spring Boot (3.4.4)
Framework principal para desenvolvimento de aplicações Java baseadas em Spring, proporcionando configuração automática, embedded servers e outras facilidades.

### Spring Data Redis
Integração com Redis para caching, permitindo armazenar e recuperar dados de forma eficiente e com baixa latência.

### Spring Cloud OpenFeign
Cliente HTTP declarativo que facilita a integração com APIs REST externas (Coinpaprika e Currencylayer) através de interfaces Java.

### Lombok
Reduz o boilerplate code através de anotações, melhorando a legibilidade e manutenibilidade do código.

### JUnit
Framework de testes unitários utilizado para garantir a qualidade e estabilidade do código.

### Maven
Ferramenta de automação de build e gerenciamento de dependências.

### Docker e Docker Compose
Ferramentas para containerização, permitindo empacotar a aplicação e suas dependências de forma consistente para diferentes ambientes.
