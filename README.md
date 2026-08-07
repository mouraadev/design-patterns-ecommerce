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

## Estrutura do projeto

```
design-patterns-ecommerce/
├── pom.xml
├── .gitignore
├── README.md
└── src/main/java/com/ecommerce/
    ├── Main.java                     # demonstra todos os padrões em conjunto
    ├── builder/Pedido.java
    ├── factory/{Produto,Eletronico,Livro,Roupa,ProdutoFactory}.java
    ├── decorator/{Item,ProdutoItem,ItemDecorator,EmbalagemPresente,SeguroEnvio}.java
    ├── strategy/{EstrategiaPagamento,PagamentoCartaoCredito,PagamentoBoleto,PagamentoPix}.java
    ├── observer/{ObservadorPedido,NotificadorEmail,NotificadorSms}.java
    ├── chain/{ValidadorPedido,ValidadorEstoque,ValidadorEndereco,ValidadorFraude}.java
    ├── facade/PedidoFacade.java
    ├── singleton/ConfiguracaoSistema.java
    └── enums/StatusPedido.java
```

## Como executar

Requer **JDK 17+**.

### Opção 1 — sem Maven (javac direto)
```bash
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
java -cp out com.ecommerce.Main
```

### Opção 2 — com Maven
```bash
mvn compile exec:java -Dexec.mainClass="com.ecommerce.Main"
# ou gerar o JAR executável:
mvn package
java -jar target/design-patterns-ecommerce.jar
```

## Exemplo de saída

```
Pedido PED-001 - Cliente: Maria Silva
  - Notebook Gamer + Seguro de envio (R$ 4524.90)
  - Clean Code + Embalagem para presente (R$ 99.80)
  - Camiseta DIO (R$ 59.90)
Total: R$ 4684.60
Status: CRIADO

[TechShop Brasil] Iniciando processamento do pedido PED-001
[Validação] Verificando estoque... OK
[Validação] Verificando endereço de entrega... OK
[Validação] Análise antifraude... OK
[TechShop Brasil] Processando pagamento via Cartão de Crédito (3x)
Cobrando R$ 4684.60 no cartão final 5678 em 3x
[E-mail] Olá Maria Silva, o status do seu pedido PED-001 mudou para: PAGAMENTO_APROVADO
[SMS] Maria Silva, pedido PED-001: PAGAMENTO_APROVADO
...
Status final do pedido: ENVIADO
```

## Publicando no GitHub

```bash
cd design-patterns-ecommerce
git init
git add .
git commit -m "Projeto final: Design Patterns aplicados a um sistema de pedidos"
git branch -M main
git remote add origin https://github.com/SEU-USUARIO/design-patterns-ecommerce.git
git push -u origin main
```

## Referências

Projeto desenvolvido como entrega do desafio de Padrões de Projeto do
bootcamp DIO, com base nos repositórios de referência da trilha:
- `github.com/digitalinnovationone/lab-padroes-projeto-java`
- `github.com/digitalinnovationone/lab-padroes-projeto-spring`
