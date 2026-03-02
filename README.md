
---

# Challenge - Production Suggestion API

This repository contains the back-end API for the **Challenge Production Module**, developed as a technical assessment.

The system manages the inventory of raw materials, product definitions (Bill of Materials - BOM), and features an optimization algorithm that suggests the most profitable production plan based on current stock availability.

---

## 🎯 Project Overview & Requirements Met

This project was built to fulfill the following requirements:

* **Separation of Concerns (RNF002):** Fully decoupled RESTful API architecture.
* **Database (RNF004):** Uses an Oracle Database.
* **Framework (RNF005):** Built with **Quarkus**, i'm following a strict layered architecture (`Controller`, `Service`, `Repository`, `Model`, `Util`).
* **Language (RNF007):** Entire codebase, database schemas, and documentation are written in English.
* **Functionality (RF001 - RF004):** Complete CRUD operations for Products, Raw Materials, BOM associations, and a dedicated endpoint for production suggestions.

---

## 🛠️ Technologies & Stack

* **Java 17**
* **Quarkus 3.x**
* **Hibernate ORM with Panache** (JPA implementation)
* **Oracle Database 23c Free** (Docker)
* **JUnit 5 & REST Assured** (Unit and API Integration Testing)
* **Mockito** (Mocking for isolated tests)

---

## 🏗️ Architecture

The application strictly follows a layered design to ensure maintainability and separation of concerns:

* **Models (`models` / `models.dto`)**
  Domain entities containing JPA mappings and UUID generation. DTOs are used to transfer data without exposing internal database structures or triggering infinite recursion in JSON serialization.

* **Repositories (`repository`)**
  Data access layer using the Active Record / Repository pattern via Panache.

* **Services (`service`)**
  Business logic layer, managing database transactions (`@Transactional`), complex associations between products and materials, and data mapping.

* **Controllers (`controller`)**
  JAX-RS Resources exposing the REST APIs.

* **Utils (`util`)**
  Contains the core logic for the Production Suggestion Algorithm, completely isolated from framework dependencies for easy unit testing.

---

## ⚙️ How to Run the Project

### Prerequisites

* **Docker** installed
* **Java 17+** installed
* **Maven** (optional — you can use the provided `./mvnw` wrapper)

---

### Step 1: Start the Oracle Database

A `docker-compose.yml` file is provided at the root of the project.

Run:

```bash
docker compose up -d
```

> Wait a few moments for the database to fully initialize and accept connections.

---

### Step 2: Run the Quarkus Application

> **Tip (First Run Only):** Before starting the application for the first time, set the database generation strategy to `update` in `src/main/resources/application.properties` so Hibernate automatically creates the required tables:
> ```properties
> quarkus.hibernate-orm.database.generation=update
> ```
> After the first run and the schema is created, revert it back to `none` to prevent unintended schema changes on subsequent startups.

Start the application in development mode (which enables live coding and continuous testing):

```bash
./mvnw compile quarkus:dev
```

> **Note:** Upon the first startup, the `DataBaseSeeder` will automatically populate the database with initial data based on a **Plastics Manufacturing domain** (e.g., HDPE Resin, Blue Masterbatch, Premium Plastic Chairs, and Heavy-Duty Buckets).
> This allows you to test the API and the algorithm immediately without manual data entry.

---

## 🧪 Running Tests

The application includes robust tests for:

* Core utility algorithms (business rules)
* REST controllers (using mocked services to isolate the HTTP layer)

To run the full test suite:

```bash
./mvnw test
```

> **Tip:** If running Quarkus in Dev Mode (`quarkus:dev`), simply press the `r` key in the terminal to execute the tests instantly.

---

## 🧠 The Optimization Algorithm

The core feature of this API is the **Production Suggestion Algorithm**, accessible via:

```
GET /api/products/suggestions
```

The algorithm uses a **Greedy approach** to maximize expected revenue:

1. Sorts all available products by their unit price in descending order (highest value first).
2. Iterates through the products and calculates the maximum quantity that can be produced based on the current stock of required raw materials (identifying the bottleneck material).
3. Deducts the consumed materials from a simulated in-memory stock map.
4. Proceeds to the next product using the remaining stock, ensuring high-value items are prioritized.
5. Returns a detailed DTO containing:

   * Suggested quantities for each product
   * Total expected financial value

---

## 📡 API Endpoints

All endpoints consume and produce `application/json`.

### Raw Materials API (`/api/raw-materials`)

* `GET /api/raw-materials` → List all raw materials
* `POST /api/raw-materials` → Create a new raw material
* `PUT /api/raw-materials/{code}` → Update an existing raw material (searches by UUID)
* `DELETE /api/raw-materials/{code}` → Delete a raw material

---

### Products API (`/api/products`)

* `GET /api/products` → List all products and their required raw materials (BOM)
* `POST /api/products` → Create a new product and associate materials
* `PUT /api/products/{code}` → Update a product and its associations
* `DELETE /api/products/{code}` → Delete a product

---

### Production Suggestions API

* `GET /api/products/suggestions` → Calculate and return the optimal production plan and total estimated value based on current stock.

---

## 🏆 Developed for Technical Assessment