# Problem Statement
RCS customer onboarding process, Customer Order Forms and Qualification Forms are used to collect essential information required to onboard enterprise customers. Today, these forms are shared manually and tracked offline, which makes validation, versioning, and workflow management inefficient.

You are required to design and implement a Spring Boot–based backend service that enables the creation, submission, validation, and tracking of RCS Customer Order Forms and Qualification Forms, ensuring a structured and auditable onboarding workflow.

Functional Requirements

1. Form Management
	a) Create and manage two form types:
		1) Customer Order Form
		2) Qualification Form
	b) Support versioning of forms.
	c) Allow retrieval of the latest and historical versions.
2. Customer Submission
	a) Provide REST APIs for customers (or internal teams) to submit completed forms.
	b) Validate mandatory fields and business rules before acceptance.
	c) Persist submissions with status (Draft, Submitted, In Review, Approved, Rejected).
3. Workflow & Review
	a) Enable TPM/Sales teams to:
		1) Review submitted forms
		2) Approve or reject submissions with remarks
	b) Maintain an audit trail of status changes.
4. Data Access
	a) APIs to fetch submissions by:
		Customer
		Status
		Date range
	b) Role-based access (Customer, TPM, Sales, Admin).

# RCS Backend Service

A Spring Boot 3.x backend service for managing users, customers, and form submissions with:
- JWT-based authentication
- Role-based access control (RBAC)
- Pageable REST endpoints
- Swagger/OpenAPI documentation

---

## Features
- **Authentication & Authorization**
  - JWT token generation and validation
  - Role-based access using `@PreAuthorize`
  - Default roles: `ROLE_ADMIN`, `ROLE_TPM`, `ROLE_SALES`, `ROLE_CUSTOMER`

- **User Management**
  - Register new users with roles
  - Link users to customers
  - List users with pageable + sorting support

- **Form Submissions**
  - Schema-driven validation
  - Filter submissions by customer, status, and date range
  - Pageable search with DTO mapping

- **Swagger Integration**
  - Auto-generated API docs
  - Interactive testing of endpoints
  - Pageable parameters (`page`, `size`, `sort`) exposed in Swagger UI

---

## Tech Stack
- Java 17+
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA
- Hibernate
- Swagger/OpenAPI (springdoc-openapi)
- Maven

---

## Setup

### 1. Clone the repository
git clone https://github.com/bipin-kum/RCS-COMS-BE.git
