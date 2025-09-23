
# Backend for Pluto Payments
A lightweight backend application for a **credit card provider** that serves both hardware and frontend.
The application implements function such as session based logins, mTLS connection with the Hardware Layer
and HMAC checker for integrity. This application is part of the larger **Pluto Payments** ecosystem, which also
includes a database, hardware and frontend.

For the full project, check out the main repository: [Pluto Payments](https://github.com/lafftale1999/pluto_payments)

## About
The application contains the following function:
* **Networking**:
  * 8080 for requests through /api/*
  * 443 for confidential requests through /device/*
* **Session Based Login**: 30 minute sessions
* **DTOs**: For data sharing control
* **Security**:
  * mTLS connection on /device/*
  * HMAC control with device key
  * SHA256 hash for sensitive data

## Usage
After following the setup guide down below, these are the following defined APIs and their functions:

### `/api/auth/login`
Needs a LoginRequest defined in [`LoginRequest`](src/main/java/com/otulp/pluto_payments_backend/DTOs/LoginRequest.java).
Controls that the user exists in the database and verifies the password. If the password is correct
a session will be created and returns a cookie to the caller.

### `/api/auth/logout`
Destroys the current session. The user needs to log back in.

### `/api/auth/account/change_password`
Receives a PasswordChangeDTO defined in [`PasswordChangeDTO`](src/main/java/com/otulp/pluto_payments_backend/DTOs/PasswordChangeDTO.java).
Returns a ResponseEntity with a String containing the result message. Will either return a 400 (wrong password) or 200 (correct password).

### `/api/auth/account/card/me`
Returns the card of the currently logged-in user.

### `/api/auth/account/get_invoice/{invoiceId}`
Returns the Invoice information with Long id {invoiceId}.

### `/api/auth/account/get_invoices`
Returns an overview of invoices connected to the user.

### `/api/auth/account/get_account_d`
Returns a detailed overview of the currently logged-in account and the information tied to it.

## Dependencies

### JVM (Java Virtual Machine)
Needed to run the software.

### JDK (Java Development Kit 17+)
We used JDK 17 for this project. Needed if you want to develop the application.

### Maven
Needed to build the software and download all related dependencies in [`pom.xml`](pom.xml).

## Build project

### 1. Clone project
```shell
git clone https://github.com/lafftale1999/pluto_payments_backend.git
```

### 2. MySQL Workbench
Launch MySQL Workbench and run the following scripts:

**2.1. Create database**
```sql
DROP DATABASE pluto;
CREATE DATABASE pluto;
USE pluto;
```

**2.2. Create user**
```sql
CREATE USER 'pluto_banker'@'localhost' IDENTIFIED BY 'pluto123';
GRANT ALL PRIVILEGES ON pluto.* TO 'pluto_banker'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Seed project
Open [`PlutoPaymentsBackendApplication`](src/main/java/com/otulp/pluto_payments_backend/PlutoPaymentsBackendApplication.java)

Uncomment the whole function 
```java
@Bean
public CommandLineRunner fill (DeviceRepository repo, CountryRepo countryRepo, AddressRepo addressRepo,
                               CardRepo cardRepo, CustomerRepo customerRepo, InvoiceRepo invoiceRepo,
                               TransactionRepo transactionRepo) {}
```
> **Note:** Remember to re-comment it after running the application to avoid seeding multiple of the same values.

### 4. Generate certificates
For the `/device/*` API, the server is configured to force mTLS connection. To generate both CA, server and client cert,
you can use the script in [`scripts/certificate_generator.sh`](scripts/certificate_generator.sh).

Make sure to set the following variables to match your needs:

```shell
CERT_LIFETIME=3650

# CERTIFICATE AUTHORITY VARIABLES
CA_DIR=../src/main/resources/authorization/ca
CA_CN="pluto_payments"
CA_FILE_NAME="ca"

# SERVER VARIABLES
GEN_SERVER=1 # if 1 will generate server certificates
SERVER_DIR=../src/main/resources/authorization/server
SERVER_FILE_NAME="server"
SERVER_CN="pluto_payments"

# CLIENT VARIABLES
GEN_CLIENT=1  # if 1 will generate client certificates
GEN_CLIENT_PEM=1 # if 1 will generate .pem files for client
CLIENT_DIR=../src/main/resources/authorization/client
CLIENT_NAME="admin"
CLIENT_PASSWORD="admin123"
CLIENT_FILE_NAME="client"
CLIENT_CN="client"

# KEYSTORE VARIABLES
KEYSTORE_DIR=../src/main/resources/authorization/keystore
KEYSTORE_ALIAS="admin"
KEYSTORE_PASS="admin123"

# TRUSTSTORE VARIABLES
TRUSTSTORE_ALIAS="admin"
TRUSTSTORE_PASS="admin123"
TRUSTSTORE_DIR=../src/main/resources/authorization/truststore
```

After that run this script in a bash environment or change the shebang to your environment.

> **Note:** The server build will fail without having the certificates.
> 
### 5. Run the project
Run [`PlutoPaymentsBackendApplication`](src/main/java/com/otulp/pluto_payments_backend/PlutoPaymentsBackendApplication.java)

## About us
This project has been created by [`Maksym`](https://github.com/Zar000), [`Johann`](https://github.com/hager3737) and [`Carl`](https://github.com/lafftale1999).
