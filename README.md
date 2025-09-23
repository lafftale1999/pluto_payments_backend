
# Backend for Pluto Payments
A lightweight backend application for a **credit card provider** that serves both hardware and frontend.
The application implements function such as session based logins, mTLS connection with the Hardware Layer
and HMAC checker for integrity. This application is part of the larger **Pluto Payments** ecosystem, which also
includes a database, hardware and frontend.

For the full project, check out the main repository: [Pluto Payments](https://github.com/lafftale1999/pluto_payments)

## About
The application contains the following function:
* **Networking**:
  * 8080 for requests through /app/*
  * 443 for confidential requests through /device/*
* **Session Based Login**: 30 minute sessions
* **DTOs**: For data sharing control
* **Security**:
  * mTLS connection on /device/*
  * HMAC control with device key
  * SHA256 hash for sensitive data

## Usage
After following the setup guide down below, these are the following defined APIs and their functions:

### /app

## Libs & Software
Maven & Hibernate & Springboot & MySQL Workbench & Lombok

## Links to related project files
[Front end](https://github.com/lafftale1999/pluto_payments_frontend)

[Hardware](https://github.com/lafftale1999/pluto_payments_hardware)


In this project we have created the following :
- Database
- Frontend
- ESP32 device for making transactions
- Backend

To run the project and see data without any front end you need to do the following :
1. Clone the repository
2. Launch MySQL Workbench
3. Run this code in MySQL
   ```sql
    CREATE USER 'pluto_banker'@'localhost' IDENTIFIED BY 'pluto123';
    GRANT ALL PRIVILEGES ON pluto.* TO 'pluto_banker'@'localhost';
    FLUSH PRIVILEGES;
    create database pluto;
   ```
4. Now head over to [..]\src\main\java\com\otulp\pluto_payments_backend\PlutoPaymentsBackendApplication.java
5. Uncomment lines 19 through 59
6. Run the project
7. You should now comment the previously uncommented lines
8. Now you can visit localhost:8080 in your browser and add various lines to test! Example:
  - localhost:8080/api/account/get_invoice

-Check the [..]\src\main\java\com\otulp\pluto_payments_backend\Controllers\AccountController.java For other pages!
