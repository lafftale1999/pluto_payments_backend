
# Backend for Pluto Payments

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
4. Now head over to [..]\src\main\java\com\otulp\pluto_payments_backend.java
5. Uncomment lines 19 through 59
6. Run the project
7. You should now comment the previously uncommented lines
8. Now you can visit localhost:8080 in your browser and add various lines to test! Example:
  - localhost:8080/api/account/get_invoice

-Check the [..]src\main\java\com\otulp\pluto_payments_backend\Controllers.java For other pages!
