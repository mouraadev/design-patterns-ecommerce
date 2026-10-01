# Sistema de Pedidos — Design Patterns em Java

Projeto final do desafio **"Padrões de Projeto"** do bootcamp (DIO). O objetivo
era consolidar na prática os padrões de projeto vistos em aula. Em vez de
reproduzir exemplos soltos, este repositório implementa **8 padrões de projeto
integrados em um único domínio coeso**: um sistema simplificado de pedidos de
e-commerce.

## Padrões implementados

| Padrão | Onde está | O que resolve aqui |
|---|---|---|
| **Builder** | `builder/Pedido.java` | Constrói um `Pedido` passo a passo (cliente, endereço, itens) com uma API fluente. |
| **Factory Method** | `factory/ProdutoFactory.java` | Cria diferentes tipos de produto (`Eletronico`, `Livro`, `Roupa`) sem expor as classes concretas ao cliente. |
| **Decorator** | `decorator/*` | Adiciona serviços extras a um item do pedido (embalagem de presente, seguro de envio) dinamicamente, sem alterar as classes de produto. |
| **Strategy** | `strategy/*` | Permite trocar a forma de pagamento (cartão, boleto, Pix) em tempo de execução. |
| **Observer** | `observer/*`, `builder/Pedido.java` | Notifica automaticamente (e-mail, SMS) o cliente a cada mudança de status do pedido. |
| **Chain of Responsibility** | `chain/*` | Executa validações em cadeia (estoque → endereço → antifraude) antes de aprovar o pedido. |
| **Facade** | `facade/PedidoFacade.java` | Expõe um único método (`finalizarPedido`) que orquestra toda a complexidade dos padrões acima. |
| **Singleton** | `singleton/ConfiguracaoSistema.java` | Garante uma única instância de configuração/log do sistema, acessível globalmente. |

## Qualidade e arquitetura

Além dos oito padrões, o projeto inclui as melhorias levantadas na revisão de
código:

- testes unitários com JUnit e Mockito para todos os padrões;
- inversão de dependência na cadeia por meio da interface `ValidadorPedido`;
- exceções de domínio para pedido inválido, falta de estoque e pagamento recusado;
- cópias defensivas, campos finais e Singleton thread-safe;
- logging com SLF4J, sem chamadas diretas a `System.out`;
- relatório e limite mínimo de cobertura com JaCoCo;
- integração contínua com GitHub Actions.

## Estrutura do projeto

```
design-patterns-ecommerce/
├── pom.xml
├── .github/workflows/ci.yml          # build e testes em cada PR/push
├── .gitignore
├── README.md
└── src/
    ├── main/java/com/ecommerce/
    │   ├── Main.java                 # demonstra todos os padrões em conjunto
    │   ├── builder/Pedido.java
    │   ├── factory/*
    │   ├── decorator/*
    │   ├── strategy/*
    │   ├── observer/*
    │   ├── chain/*
    │   ├── facade/PedidoFacade.java
    │   ├── singleton/ConfiguracaoSistema.java
    │   ├── exception/*
    │   └── enums/StatusPedido.java
    └── test/java/com/ecommerce/      # oito suítes de testes unitários
```

## Como executar

Requer **JDK 17+** e **Maven 3.9+**.

```bash
# compilar, testar, verificar cobertura e gerar o JAR executável
mvn clean verify

# executar a demonstração
java -jar target/design-patterns-ecommerce.jar
```

O relatório de cobertura é gerado em `target/site/jacoco/index.html`. O build
falha se a cobertura de linhas do projeto cair abaixo de 70%.

## Exemplo de saída

```text
[main] INFO com.ecommerce.Main - Pedido PED-001 - Cliente: Maria Silva
  - Notebook Gamer + Seguro de envio (R$ 4524.90)
  - Clean Code + Embalagem para presente (R$ 99.80)
  - Camiseta DIO (R$ 59.90)
Total: R$ 4684.60
Status: CRIADO

[main] INFO com.ecommerce.facade.PedidoFacade - [TechShop Brasil] Iniciando processamento do pedido PED-001
[main] INFO com.ecommerce.chain.ValidadorEstoque - Estoque validado para o pedido PED-001
[main] INFO com.ecommerce.chain.ValidadorEndereco - Endereço validado para o pedido PED-001
[main] INFO com.ecommerce.chain.ValidadorFraude - Análise antifraude aprovada para o pedido PED-001
[main] INFO com.ecommerce.facade.PedidoFacade - [TechShop Brasil] Processando pagamento via Cartão de Crédito (3x)
[main] INFO com.ecommerce.strategy.PagamentoCartaoCredito - Cobrando R$ 4684.60 no cartão final 5678 em 3x
...
[main] INFO com.ecommerce.Main - Status final do pedido: ENVIADO
```

## Referências

Projeto desenvolvido como entrega do desafio de Padrões de Projeto do
bootcamp DIO, com base nos repositórios de referência da trilha:
- [Lab Padrões de Projeto Java](https://github.com/digitalinnovationone/lab-padroes-projeto-java)
- [Lab Padrões de Projeto Spring](https://github.com/digitalinnovationone/lab-padroes-projeto-spring)
