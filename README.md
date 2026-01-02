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

### 2. MySQL DB File

rcs.sql

### 3. Swagger UI : API and documentation

http://localhost:8080/swagger-ui.html

### 4. Roles & Access

ADMIN → Full access
TPM → Submission review
SALES → Customer & submission access
CUSTOMER → Own submissions only

### 5. Sample request/response payloads


#### 1. Register User 

TYPE: POST

URL : /api/auth/register

Request

```json 
{
  "username": "bipin_admin",
  "password": "bipin_admin",
  "roleNames": ["ROLE_ADMIN"]
}
```
Response: OK

#### 2. Login User 

TYPE: POST

URL : /api/auth/login

Request :

```json 
{
  "userName": "bipin_sales",
  "password": "bipin_sales"
}
```

Response : 

```json 
{
	eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaXBpbl9zYWxlcyIsImlzcyI6InJjcyIsImlhdCI6MTc2NzM0NjUyNCwiZXhwIjoxNzY3MzUwMTI0LCJyb2xlcyI6WyJST0xFX1NBTEVTIl19.exwyLjLyeYGvpI9DFleHrziLbiNdIIZUgw0NT6AtLns
}
```

#### 3. Roles

TYPE: GET

URL : /api/admin/roles

Response

```json
[
  "ROLE_ADMIN",
  "ROLE_CUSTOMER",
  "ROLE_SALES",
  "ROLE_TPM"
]
```

#### 4. Create User

TYPE: POST

URL: /api/admin/users

Request:

```json
{
  "userName": "bipin_sales",
  "password": "bipin_sales",
  "roleIds": [
    3
  ]
}
```

Response: 

```json
{
  "id": 4,
  "userName": "bipin_sales",
  "enabled": true,
  "roles": [
    "ROLE_SALES"
  ]
}
```

#### 5. Get Users

TYPE: GET

URL: /api/admin/users?page=0&size=10

Response:

```json
{
  "content": [
    {
      "id": 2,
      "userName": "bipin_admin",
      "enabled": true,
      "roles": [
        "ROLE_ADMIN"
      ]
    },
    {
      "id": 3,
      "userName": "bipin_customer",
      "enabled": true,
      "roles": [
        "ROLE_CUSTOMER"
      ]
    },
    {
      "id": 4,
      "userName": "bipin_sales",
      "enabled": true,
      "roles": [
        "ROLE_SALES"
      ]
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 3,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 3,
  "empty": false
}
```

#### 6. Get User

TYPE: GET

URL : /api/admin/users/2

Response:

```json
{
  "id": 2,
  "userName": "bipin_admin",
  "enabled": true,
  "roles": [
    "ROLE_ADMIN"
  ]
}
```

#### 7. Create Customer

Type: POST

URL: /api/customers

Request:

```json
{
  "name": "Test"
}
```

Response:

```json
{
  "id": 2,
  "name": "Test"
}
```

#### 8. Update Customer

TYPE: PUT

URL: /api/customers/2

Request:

```json
{
  "name": "BiCS"
}
```

Response:

```json
{
  "id": 2,
  "name": "BiCS"
}
```

#### 9. Get Customer by ID

TYPE: GET

URL: /api/customers/2

Response:

```json 
{
  "id": 2,
  "name": "BiCS"
}
```

#### 10. Get Customers

TYPE: GET

URL: /api/customers?page=0&size=10

Response: 

```json
{
  "content": [
    {
      "id": 1,
      "name": "Tata Communications"
    },
    {
      "id": 2,
      "name": "BiCS"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 2,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```

#### 11. Create Form : ORDER

TYPE: POST

URL: /api/forms

Request: 

```json 
{
  "type": "ORDER",
  "version": 2,
  "schemaJson": "{\"required\":[\"customerName\",\"customerCode\",\"contactEmail\",\"orderDate\"],\"fields\":{\"customerName\":{\"type\":\"string\",\"minLength\":3},\"customerCode\":{\"type\":\"string\",\"pattern\":\"^[A-Z0-9]{5,10}$\"},\"contactEmail\":{\"type\":\"string\",\"pattern\":\"^[^@\\\\s]+@[^@\\\\s]+\\\\.[^@\\\\s]+$\"},\"orderDate\":{\"type\":\"string\",\"pattern\":\"^\\\\d{4}-\\\\d{2}-\\\\d{2}$\"},\"orderAmount\":{\"type\":\"number\"},\"industry\":{\"type\":\"string\",\"enum\":[\"Finance\",\"Retail\",\"Telecom\",\"Manufacturing\"]}}}"
}
```

Response: 

```json 
{
  "id": 4,
  "type": "ORDER",
  "version": 2,
  "schemaJson": "{\"required\":[\"customerName\",\"customerCode\",\"contactEmail\",\"orderDate\"],\"fields\":{\"customerName\":{\"type\":\"string\",\"minLength\":3},\"customerCode\":{\"type\":\"string\",\"pattern\":\"^[A-Z0-9]{5,10}$\"},\"contactEmail\":{\"type\":\"string\",\"pattern\":\"^[^@\\\\s]+@[^@\\\\s]+\\\\.[^@\\\\s]+$\"},\"orderDate\":{\"type\":\"string\",\"pattern\":\"^\\\\d{4}-\\\\d{2}-\\\\d{2}$\"},\"orderAmount\":{\"type\":\"number\"},\"industry\":{\"type\":\"string\",\"enum\":[\"Finance\",\"Retail\",\"Telecom\",\"Manufacturing\"]}}}",
  "isActive": true,
  "createdBy": {
    "id": 2,
    "userName": "bipin_admin"
  },
  "createdAt": "2026-01-02T16:31:53.6942869"
}
```

#### 12. Create Form : QUALIFICATION

TYPE: POST

URL :/api/forms

Request: 

```json 
{
  "type": "QUALIFICATION",
  "version": 2,
  "schemaJson": "{\"required\":[\"customerName\",\"qualificationDate\",\"businessSize\",\"region\"],\"fields\":{\"customerName\":{\"type\":\"string\",\"minLength\":3},\"qualificationDate\":{\"type\":\"string\",\"pattern\":\"^\\\\d{4}-\\\\d{2}-\\\\d{2}$\"},\"businessSize\":{\"type\":\"string\",\"enum\":[\"Small\",\"Medium\",\"Large\",\"Enterprise\"]},\"region\":{\"type\":\"string\",\"enum\":[\"APAC\",\"EMEA\",\"AMERICAS\"]},\"annualRevenue\":{\"type\":\"number\"},\"contactPhone\":{\"type\":\"string\",\"pattern\":\"^[0-9]{10}$\"}}}"
}
```

Response: 

```json
{
  "id": 3,
  "type": "QUALIFICATION",
  "version": 2,
  "schemaJson": "{\"required\":[\"customerName\",\"qualificationDate\",\"businessSize\",\"region\"],\"fields\":{\"customerName\":{\"type\":\"string\",\"minLength\":3},\"qualificationDate\":{\"type\":\"string\",\"pattern\":\"^\\\\d{4}-\\\\d{2}-\\\\d{2}$\"},\"businessSize\":{\"type\":\"string\",\"enum\":[\"Small\",\"Medium\",\"Large\",\"Enterprise\"]},\"region\":{\"type\":\"string\",\"enum\":[\"APAC\",\"EMEA\",\"AMERICAS\"]},\"annualRevenue\":{\"type\":\"number\"},\"contactPhone\":{\"type\":\"string\",\"pattern\":\"^[0-9]{10}$\"}}}",
  "isActive": true,
  "createdBy": {
    "id": 2,
    "userName": "bipin_admin"
  },
  "createdAt": "2026-01-02T16:30:54.2798175"
}
```

#### 13. Get Forms by type AND/OR latest version

TYPE: GET

URL: /api/forms?type=QUALIFICATION&latest=true

Response:

```json
[
  {
    "id": 2,
    "type": "QUALIFICATION",
    "version": 1,
    "isActive": true
  }
]
```

#### 14. Get Form by ID

TYPE: GET

URL: /api/forms/1

Response: 

```json
{
  "id": 1,
  "type": "ORDER",
  "version": 1,
  "schemaJson": "{\"required\":[\"customerName\",\"customerCode\",\"contactEmail\",\"orderDate\"],\"fields\":{\"customerName\":{\"type\":\"string\",\"minLength\":3},\"customerCode\":{\"type\":\"string\",\"pattern\":\"^[A-Z0-9]{5,10}$\"},\"contactEmail\":{\"type\":\"string\",\"pattern\":\"^[^@\\\\s]+@[^@\\\\s]+\\\\.[^@\\\\s]+$\"},\"orderDate\":{\"type\":\"string\",\"pattern\":\"^\\\\d{4}-\\\\d{2}-\\\\d{2}$\"},\"orderAmount\":{\"type\":\"number\"},\"industry\":{\"type\":\"string\",\"enum\":[\"Finance\",\"Retail\",\"Telecom\",\"Manufacturing\"]}}}",
  "isActive": true,
  "createdBy": {
    "id": 2,
    "userName": "bipin_admin"
  },
  "createdAt": "2026-01-02T14:38:06.118442"
}
```

#### 14. Create Submission Form

TYPE: POST

URL: /api/submissions

Request:

```json
{
  "formTemplateId": 2,
  "customerId": 1,
  "dataJson": "{\"customerName\":\"TataCommunications\",\"qualificationDate\":\"2026-01-02\",\"businessSize\":\"Enterprise\",\"region\":\"EMEA\",\"annualRevenue\":500000000,\"contactPhone\":\"9876543210\"}",
  "status": "DRAFT"
}
```

Response: 

```json
{
  "id": 2,
  "formTemplateId": 2,
  "customerId": 1,
  "status": "DRAFT",
  "dataJson": "{\"customerName\":\"TataCommunications\",\"qualificationDate\":\"2026-01-02\",\"businessSize\":\"Enterprise\",\"region\":\"EMEA\",\"annualRevenue\":500000000,\"contactPhone\":\"9876543210\"}",
  "createdBy": {
    "id": 4,
    "userName": "bipin_sales"
  },
  "createdAt": "2026-01-02T15:14:37.6921306",
  "updatedBy": {
    "id": 4,
    "userName": "bipin_sales"
  },
  "updatedAt": "2026-01-02T15:14:37.6921306"
}
```

#### 15. Update Submission Form

TYPE: PUT

URL: /api/submissions/2

Request: 

```json 
{
  "dataJson": "{\"customerName\":\"TataCommunications\",\"qualificationDate\":\"2026-01-02\",\"businessSize\":\"Enterprise\",\"region\":\"EMEA\",\"annualRevenue\":500000000,\"contactPhone\":\"9876543210\"}",
  "status": "IN_REVIEW"
}
```

Response: 

```json
{
  "id": 2,
  "formTemplateId": 2,
  "customerId": 1,
  "status": "IN_REVIEW",
  "dataJson": "{\"customerName\":\"TataCommunications\",\"qualificationDate\":\"2026-01-02\",\"businessSize\":\"Enterprise\",\"region\":\"EMEA\",\"annualRevenue\":500000000,\"contactPhone\":\"9876543210\"}",
  "createdBy": {
    "id": 4,
    "userName": "bipin_sales"
  },
  "createdAt": "2026-01-02T15:14:37.692131",
  "updatedBy": {
    "id": 4,
    "userName": "bipin_sales"
  },
  "updatedAt": "2026-01-02T15:15:13.964505"
}
```

#### 16. APPROVE/REJECT Submission Form

TYPE: POST

URL: /api/submissions/2/review

Request: 

```json 
{
  "action": "APPROVED",
  "remarks": "Test APPROVED"
}
```

Response: 

```json 
{
  "id": 2,
  "formTemplateId": 2,
  "customerId": 1,
  "status": "APPROVED",
  "dataJson": "{\"customerName\":\"TataCommunications\",\"qualificationDate\":\"2026-01-02\",\"businessSize\":\"Enterprise\",\"region\":\"EMEA\",\"annualRevenue\":500000000,\"contactPhone\":\"9876543210\"}",
  "createdBy": {
    "id": 4,
    "userName": "bipin_sales"
  },
  "createdAt": "2026-01-02T15:14:37.692131",
  "updatedBy": {
    "id": 4,
    "userName": "bipin_sales"
  },
  "updatedAt": "2026-01-02T15:15:24.0432261"
}
```

#### 17. Search Submission Form

TYPE: GET

URL: /api/submissions/search?customerId=1&status=APPROVED&startDate=2026-01-02T00%3A00%3A00&endDate=2026-01-02T23%3A00%3A00&page=0&size=10

Response: 

```json 
{
  "content": [
    {
      "id": 1,
      "customerId": 1,
      "status": "APPROVED",
      "createdAt": "2026-01-02T14:57:59.514512"
    },
    {
      "id": 2,
      "customerId": 1,
      "status": "APPROVED",
      "createdAt": "2026-01-02T15:14:37.692131"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 2,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```