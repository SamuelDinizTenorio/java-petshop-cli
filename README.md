# 🐾 Java PetShop CLI

Aplicação de linha de comando (CLI) para simular o gerenciamento de uma **máquina automatizada de banho para pets**, desenvolvida como projeto prático do bootcamp da [DIO](https://www.dio.me/).

---

## 📋 Sobre o Projeto

O sistema simula uma máquina de banho para animais de estimação, permitindo ao usuário interagir via terminal para realizar operações como:

- Dar banho em um pet
- Colocar e retirar pets da máquina
- Monitorar e reabastecer os níveis de água e shampoo
- Limpar a máquina após o uso

Toda a lógica de negócio é validada por **exceções customizadas**, garantindo que regras como capacidade máxima, máquina suja e estoque insuficiente sejam respeitadas em tempo de execução.

---

## 🚀 Funcionalidades

| Opção | Descrição |
|-------|-----------|
| `0`   | Sair do sistema |
| `1`   | Dar banho no pet *(consome 10L de água e 2L de shampoo)* |
| `2`   | Abastecer água *(+2L por vez, máximo 30L)* |
| `3`   | Abastecer shampoo *(+2L por vez, máximo 10L)* |
| `4`   | Verificar nível de água |
| `5`   | Verificar nível de shampoo |
| `6`   | Verificar se há pet na máquina |
| `7`   | Colocar pet na máquina |
| `8`   | Retirar pet da máquina |
| `9`   | Limpar a máquina *(consome 3L de água e 1L de shampoo)* |

---

## 🏗️ Estrutura do Projeto

```
src/
├── main/java/com/samuel/
│   ├── Main.java                          # Ponto de entrada da aplicação
│   ├── cli/
│   │   └── PetShopMenu.java               # Interface de menu CLI
│   ├── model/
│   │   ├── Pet.java                       # Entidade Pet
│   │   └── PetBathingMachine.java         # Máquina de banho (lógica principal)
│   └── exception/
│       ├── BusinessException.java         # Exceção base abstrata
│       ├── InsufficientStockException.java
│       ├── MachineIsCleanException.java
│       ├── MachineIsDirtyException.java
│       ├── MaximumCapacityReachedException.java
│       ├── PetIsAlreadyCleanException.java
│       ├── PetIsAlreadyInMachineException.java
│       └── PetNotInMachineException.java
└── test/java/
    ├── cli/
    │   └── PetShopMenuTest.java           # Testes do menu CLI
    └── model/
        ├── PetTest.java                   # Testes da entidade Pet
        └── PetBathingMachineTest.java     # Testes da máquina de banho
```

---

## 🧰 Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|---|---|---|
| **Java** | 17 | Linguagem principal |
| **Maven** | — | Gerenciamento de dependências e build |
| **JUnit Jupiter** | 5.10.2 | Framework de testes unitários |
| **AssertJ** | 3.25.3 | Asserções fluentes nos testes |

---

## 🧪 Testes

O projeto conta com uma suíte de testes unitários organizada com `@Nested` e `@DisplayName` do JUnit 5, cobrindo os principais cenários de sucesso e de erro de cada operação.

**Classes de teste:**
- `PetTest` — testa a criação e limpeza de um pet
- `PetBathingMachineTest` — testa toda a lógica da máquina (banho, abastecimento, limpeza, etc.)
- `PetShopMenuTest` — testa as opções do menu CLI com I/O simulado via `InputStream`/`PrintStream`

Para executar os testes (requer Maven com acesso à internet para baixar as dependências na primeira execução):

```bash
mvn test
```

---

## ▶️ Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.6+

### Rodando a aplicação

```bash
mvn compile exec:java -Dexec.mainClass="com.samuel.Main"
```

Ou compilando manualmente:

```bash
javac -d target/classes $(find src/main/java -name "*.java")
java -cp target/classes com.samuel.Main
```

---

## 📐 Conceitos Aplicados

- **Orientação a Objetos** — encapsulamento, herança e polimorfismo
- **Exceções customizadas** — hierarquia baseada em `BusinessException` (extends `RuntimeException`)
- **Padrão de projeto** — injeção de dependência via construtor em `PetShopMenu` (facilita testabilidade)
- **Javadoc** — documentação em inglês em todas as classes e métodos públicos
- **Testes unitários** — cobertura dos cenários de sucesso e erro com JUnit 5 + AssertJ

---

## 👤 Autor

Desenvolvido por **Samuel** como parte do bootcamp Java da [DIO](https://www.dio.me/).
