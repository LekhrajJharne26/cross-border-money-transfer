# CrossBorder Money Transfer

A full-stack **Cross Border Money Transfer System** built with Java 17, Spring Boot 3, Spring Security, JWT, Spring Data JPA, Hibernate, MySQL, Flyway, Swagger/OpenAPI, and a lightweight HTML/CSS/JavaScript frontend.

The application simulates an international money-transfer platform where users can register, authenticate securely, manage beneficiaries, select countries and banking partners, create money-transfer transactions, and view transaction history.

## 🚀 Project Overview

The application follows this workflow:

User → Register → Login → JWT Authentication → Dashboard → Manage Beneficiaries → Select Country → Select Banking Partner → Enter Transfer Details → Create Transaction → View Transaction History

The backend provides REST APIs protected by Spring Security and JWT authentication.

The frontend is built using plain HTML, CSS, and Vanilla JavaScript and is served directly by the Spring Boot application.

## 🛠️ Technology Stack

### Backend

- Java 17
- Spring Boot 3
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- MySQL 8
- Flyway
- Bean Validation
- Lombok
- Maven
- Swagger / OpenAPI

### Frontend

- HTML5
- CSS3
- Vanilla JavaScript (ES6)
- Fetch API
- Responsive Design

### Frontend does NOT require

- React
- TypeScript
- Vite
- Angular
- Vue
- Node.js
- npm

The frontend runs directly through the Spring Boot application.

## ✨ Features

### 🔐 Authentication

- User registration
- User login
- BCrypt password hashing
- JWT access-token authentication
- Stateless Spring Security
- Role-based user model
- Protected REST APIs
- 401 Unauthorized handling
- Centralized exception handling
- Request validation

### 👤 Beneficiary Management

Authenticated users can:

- Add beneficiaries
- View beneficiaries
- View individual beneficiary
- Update beneficiaries
- Delete beneficiaries

Beneficiaries are owner-scoped so one user cannot access another user's beneficiaries.

### 🌍 Countries

- View supported countries
- Active country management
- Country-based banking partner lookup

Seeded countries include:

- India
- Nepal
- Philippines
- USA

### 🏦 Banking Partners

- View banking partners
- Retrieve partners by country
- Active banking-partner filtering
- Country → Banking Partner relationship

### 💸 Money Transfer

Users can create a transaction using:

- Beneficiary
- Banking Partner
- Amount
- Currency
- Purpose
- Remarks

### 📊 Transaction History

Transaction information includes:

- Transaction number
- Beneficiary
- Banking partner
- Amount
- Currency
- Status
- Created date

### 🖥️ Frontend

The frontend includes:

- Login
- Registration
- Dashboard
- Beneficiaries
- Countries
- Banking Partners
- Send Money
- Transaction History
- Profile
- Responsive navigation
- Form validation
- Notifications
- Loading states
- Empty states
- 404 page

## 🏗️ Application Architecture

```text
                         ┌─────────────────────────┐
                         │        Browser          │
                         │   HTML / CSS / JS       │
                         └────────────┬────────────┘
                                      │
                                      │ REST / HTTP
                                      ▼
                         ┌─────────────────────────┐
                         │      Spring Boot        │
                         │          /api            │
                         └────────────┬────────────┘
                                      │
                    ┌─────────────────┼─────────────────┐
                    │                 │                 │
                    ▼                 ▼                 ▼
              Controllers        Services          Security
                    │                 │                 │
                    ▼                 ▼                 ▼
                  DTOs          Repositories        JWT Filter
                                      │
                                      ▼
                               ┌─────────────┐
                               │    MySQL    │
                               └──────┬──────┘
                                      ▲
                                      │
                               ┌──────┴──────┐
                               │   Flyway    │
                               │ Migrations  │
                               └─────────────┘
