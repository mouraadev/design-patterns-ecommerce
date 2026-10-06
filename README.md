# Order System — Design Patterns in Java

[![Java](https://img.shields.io/badge/Java-17%2B-orange)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.9%2B-blue)](https://maven.apache.org/)
[![CI](https://github.com/mouraadev/design-patterns-ecommerce/actions/workflows/ci.yml/badge.svg)](https://github.com/mouraadev/design-patterns-ecommerce/actions/workflows/ci.yml)

A learning project that applies eight design patterns to a single e-commerce
order workflow. The application creates products, adds services to items,
builds an order, performs validations, processes payment, and notifies the
customer whenever the order status changes.

## Implemented patterns

| Pattern | Implementation | Responsibility |
|---|---|---|
| Builder | `builder/Order.java` | Builds orders through a fluent API. |
| Factory (Simple Factory) | `factory/ProductFactory.java` | Centralizes the creation of electronics, books, and clothing through an enum and a static method. |
| Decorator | `decorator/*` | Adds gift wrapping and shipping insurance to items. |
| Strategy | `strategy/*` | Supports credit card, bank slip, and Pix payments. |
| Observer | `observer/*` | Simulates email and SMS notifications through logs after status changes. |
| Chain of Responsibility | `chain/*` | Chains stock, address, and fraud validations. |
| Facade | `facade/OrderFacade.java` | Coordinates validation, payment, status changes, and notifications. |
| Singleton | `singleton/SystemConfiguration.java` | Maintains a single instance of the store configuration. |

## Order workflow

```text
CREATION
  ↓
STOCK VALIDATION → ADDRESS VALIDATION → FRAUD ANALYSIS
  ↓
PAYMENT PROCESSING
  ↓
PAYMENT_APPROVED → PICKING → SHIPPED
```

If validation fails, the order receives the `CANCELLED` status. If the charge
is declined, it receives the `PAYMENT_DECLINED` status.

Each order instance can be processed only once, even when multiple facade instances
try concurrently or a caller resets its status. The atomic claim is retained after
unexpected failures to prevent an unsafe payment retry; such failures need investigation.
This protection is in memory, per object, and does not deduplicate separate objects
with the same ID or requests across processes.

A listener's runtime exception is logged without interrupting the order workflow or
the remaining listeners. Notifications are synchronous and are not retried automatically.
Status updates themselves do not enforce a transition graph or order concurrent events.

This is a teaching simulation: payment methods always approve valid amounts, inventory
validation only checks for a nonempty item list, and no email or SMS is actually sent.
The configured tax rate is not applied to totals. The factory uses the Simple Factory
variant rather than a hierarchy of Factory Method creators.

## Project quality

- 24 unit tests across eight test suites;
- JUnit 6 and Mockito for testing and dependency isolation;
- dependency inversion through the `OrderValidator` interface;
- domain exceptions for invalid orders, insufficient stock, and declined payments;
- defensive collection copies, immutable fields, and a thread-safe Singleton;
- one processing attempt per order instance and isolated notification failures;
- structured logging with SLF4J;
- JaCoCo line coverage with a minimum threshold of 70%;
- automated builds and tests with GitHub Actions;
- a self-contained executable JAR with all required runtime dependencies.

## Technologies

- Java 17+
- Maven 3.9+
- JUnit 6.1.3
- Mockito 5.24.0
- SLF4J 2.0.20
- JaCoCo 0.8.14

## Project structure

```text
design-patterns-ecommerce/
├── .github/workflows/ci.yml
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/ecommerce/
    │   ├── Main.java
    │   ├── builder/
    │   ├── chain/
    │   ├── decorator/
    │   ├── enums/
    │   ├── exception/
    │   ├── facade/
    │   ├── factory/
    │   ├── observer/
    │   ├── singleton/
    │   └── strategy/
    └── test/java/com/ecommerce/
```

## Running the project

### IntelliJ IDEA

1. Open the project directory in IntelliJ IDEA.
2. Wait for IntelliJ to import the dependencies from `pom.xml`.
3. Open `src/main/java/com/ecommerce/Main.java`.
4. Click the run button next to the `main` method.

### Terminal

From the project root, compile the application, run the tests, verify coverage,
and generate the executable JAR:

```bash
mvn clean verify
```

If the `mvn` command is unavailable, run `verify` from IntelliJ's
**Maven → Lifecycle** window or install Maven and add its `bin` directory to
your `PATH`.

Run the application:

```bash
java -jar target/design-patterns-ecommerce.jar
```

## Tests and coverage

Run only the tests:

```bash
mvn test
```

Run the complete verification process and generate the coverage report:

```bash
mvn verify
```

The HTML coverage report is generated at:

```text
target/site/jacoco/index.html
```

The build fails automatically if total line coverage falls below 70%.

## Example output

```text
[main] INFO com.ecommerce.Main - === Order System - Design Patterns Demo ===
[main] INFO com.ecommerce.Main - Order ORD-001 - Customer: Ryan Moura
  - Gaming Laptop + Shipping insurance (R$ 4524.90)
  - Clean Code + Gift wrapping (R$ 99.80)
  - DIO T-shirt (R$ 59.90)
Total: R$ 4684.60
Status: CREATED

[main] INFO com.ecommerce.chain.StockValidator - Stock validated for order ORD-001
[main] INFO com.ecommerce.chain.AddressValidator - Address validated for order ORD-001
[main] INFO com.ecommerce.chain.FraudValidator - Fraud analysis approved for order ORD-001
[main] INFO com.ecommerce.strategy.CreditCardPayment - Charging R$ 4684.60 to the card ending in 5678 in 3 installment(s)
[main] INFO com.ecommerce.Main - Final order status: SHIPPED
```

## References

- [Java Design Patterns Lab](https://github.com/digitalinnovationone/lab-padroes-projeto-java)
- [Spring Design Patterns Lab](https://github.com/digitalinnovationone/lab-padroes-projeto-spring)
