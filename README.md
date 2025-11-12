# Inventory Service

This is the **Inventory Service** for the **Order Management System (OMS)** case study at **SunKing**.  
It manages product stock levels, ensures inventory consistency, and supports order fulfillment workflows.

---

## Overview

The Inventory Service is responsible for:
- Maintaining the stock quantity of products.
- Handling inventory updates during order placement.
- Preventing overselling through locking or transaction control.
- Syncing data asynchronously with the Orders Service (via Kafka).

---

## Tech Stack

- **Java**
- **Spring Boot 3**
- **PostgreSQL** (for structured inventory data)
- **Spring Data JPA**
- **Lombok**, **ModelMapper**, **Validation**

